const API_BASE = 'http://localhost:3000/api';

const DEFAULT_DISTRACTING_SITES = [
  'youtube.com', 'instagram.com', 'facebook.com', 'twitter.com',
  'x.com', 'tiktok.com', 'reddit.com', 'netflix.com',
  'twitch.tv', '9gag.com'
];

let distractingSites = [];
let siteTimers = {};

async function fetchDistractingSites() {
  try {
    const res = await fetch(`${API_BASE}/sites/active`);
    if (res.ok) {
      distractingSites = await res.json();
    }
  } catch {
    distractingSites = DEFAULT_DISTRACTING_SITES.map(d => ({
      domain: d, name: d, maxMinutesPerSession: 15, active: true
    }));
  }
}

function getDomain(url) {
  try {
    return new URL(url).hostname.replace(/^www\./, '');
  } catch {
    return null;
  }
}

function isDistracting(domain) {
  if (!domain) return false;
  return distractingSites.some(s => s.active && domain.includes(s.domain));
}

function getSiteConfig(domain) {
  return distractingSites.find(s => s.active && domain.includes(s.domain));
}

function showAlert(domain, siteName) {
  const config = getSiteConfig(domain);
  const minutes = config ? config.maxMinutesPerSession : 15;

  chrome.notifications.create({
    type: 'basic',
    iconUrl: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAC0lEQVQIHWNgAAIABQABNjN9GQAAAAlwSFlzAAAWJQAAFiUBSVIk8AAAAA0lEQVQI12NgYPgPAAEDAQAR3X3ZAAAASUVORK5CYII=',
    title: 'Detox Digital',
    message: `Voce esta ha ${minutes} minutos no ${siteName || domain}! Hora de focar no que importa.`,
    priority: 2
  });

  fetch(`${API_BASE}/alert`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      domain: domain,
      triggeredAt: new Date().toISOString(),
      acknowledged: false
    })
  }).catch(() => {});
}

async function trackVisit(domain, durationSeconds) {
  try {
    await fetch(`${API_BASE}/track`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        domain: domain,
        visitedAt: new Date().toISOString(),
        durationSeconds: durationSeconds,
        alertTriggered: true
      })
    });
  } catch {}
}

chrome.tabs.onActivated.addListener(async ({ tabId }) => {
  try {
    const tab = await chrome.tabs.get(tabId);
    const domain = getDomain(tab.url);
    handleTabChange(domain);
  } catch {}
});

chrome.tabs.onUpdated.addListener((tabId, changeInfo, tab) => {
  if (changeInfo.url) {
    const domain = getDomain(tab.url);
    handleTabChange(domain);
  }
});

function handleTabChange(domain) {
  const now = Date.now();

  for (const prevDomain in siteTimers) {
    const timer = siteTimers[prevDomain];
    if (timer.domain !== domain) {
      const elapsed = Math.floor((now - timer.startedAt) / 1000);
      if (elapsed > 10 && isDistracting(prevDomain)) {
        trackVisit(prevDomain, elapsed);
      }
      delete siteTimers[prevDomain];
    }
  }

  if (domain && isDistracting(domain)) {
    if (!siteTimers[domain]) {
      siteTimers[domain] = { startedAt: now, alerted: false };
    }
  }
}

chrome.alarms.create('checkTimer', { periodInMinutes: 1 });

chrome.alarms.onAlarm.addListener((alarm) => {
  if (alarm.name === 'checkTimer') {
    const now = Date.now();
    for (const domain in siteTimers) {
      const timer = siteTimers[domain];
      const config = getSiteConfig(domain);
      const maxMs = (config ? config.maxMinutesPerSession : 15) * 60 * 1000;
      const elapsed = now - timer.startedAt;

      if (elapsed >= maxMs && !timer.alerted) {
        timer.alerted = true;
        showAlert(domain, config ? config.name : domain);
      }
    }
  }
});

fetchDistractingSites();
setInterval(fetchDistractingSites, 5 * 60 * 1000);
