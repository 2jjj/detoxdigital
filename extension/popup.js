const API_BASE = 'http://localhost:3000/api';

async function getCurrentTab() {
  const [tab] = await chrome.tabs.query({ active: true, currentWindow: true });
  return tab;
}

function getDomain(url) {
  try {
    return new URL(url).hostname.replace(/^www\./, '');
  } catch {
    return null;
  }
}

async function updatePopup() {
  const tab = await getCurrentTab();
  const domain = getDomain(tab.url);
  const siteDiv = document.getElementById('currentSite');

  if (!domain) {
    siteDiv.className = 'current-site safe';
    siteDiv.innerHTML = '<h3>Sem site detectado</h3><p>Navegue para um site para monitorar.</p>';
    return;
  }

  try {
    const res = await fetch(`${API_BASE}/check?domain=${encodeURIComponent(domain)}`);
    const data = await res.json();
    if (data.distracting) {
      siteDiv.className = 'current-site distracting';
      siteDiv.innerHTML = `<h3>ATENCAO</h3><p>${domain} e um site distrativo!</p>`;
    } else {
      siteDiv.className = 'current-site safe';
      siteDiv.innerHTML = `<h3>Foco OK</h3><p>${domain} nao esta na lista de distrativos.</p>`;
    }
  } catch {
    siteDiv.className = 'current-site safe';
    siteDiv.innerHTML = `<h3>${domain}</h3><p>Servidor offline. Modo local.</p>`;
  }

  try {
    const res = await fetch(`${API_BASE}/sites/active`);
    const sites = await res.json();
    document.getElementById('siteCount').textContent = sites.length;
  } catch {
    document.getElementById('siteCount').textContent = 'N/A';
  }

  try {
    const res = await fetch(`${API_BASE}/stats/today`);
    const stats = await res.json();
    document.getElementById('alertCount').textContent = stats.alerts || 0;
  } catch {
    document.getElementById('alertCount').textContent = 'N/A';
  }
}

document.getElementById('btnDashboard').addEventListener('click', () => {
  chrome.tabs.create({ url: 'http://localhost:3000/dashboard' });
});

updatePopup();
