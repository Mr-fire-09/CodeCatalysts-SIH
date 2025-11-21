const API_BASE = "http://localhost:8080/api";

const state = {
    token: "",
    aiEvents: [],
    hashChain: []
};

const headers = () => ({
    "Content-Type": "application/json",
    "Authorization": `Bearer ${state.token}`
});

document.getElementById("applicationForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const form = Object.fromEntries(new FormData(e.target).entries());
    try {
        const res = await fetch(`${API_BASE}/citizen/applications`, {
            method: "POST",
            headers: headers(),
            body: JSON.stringify(form)
        });
        const data = await res.json();
        alert(data.message + " | ID: " + data?.data?.trackingId);
        e.target.reset();
    } catch (err) {
        console.error(err);
    }
});

document.getElementById("trackForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const trackingId = new FormData(e.target).get("trackingId");
    const res = await fetch(`${API_BASE}/citizen/applications/${trackingId}`, {headers: headers()});
    const data = await res.json();
    document.getElementById("trackResult").textContent = JSON.stringify(data.data, null, 2);
    renderTimeline(trackingId);
});

async function renderTimeline(trackingId) {
    const res = await fetch(`${API_BASE}/citizen/applications/${trackingId}/timeline`, {headers: headers()});
    const data = await res.json();
    const list = document.getElementById("timeline");
    list.innerHTML = "";
    data.data.forEach(event => {
        const li = document.createElement("li");
        li.innerHTML = `<strong>${event.status}</strong> - ${event.remarks} <span>${event.timestamp}</span>`;
        list.appendChild(li);
        state.aiEvents.push(`AI reviewed ${event.status} for ${trackingId}`);
    });
    refreshAiEvents();
}

document.getElementById("feedbackForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const form = Object.fromEntries(new FormData(e.target).entries());
    const res = await fetch(`${API_BASE}/feedback/${form.trackingId}`, {
        method: "POST",
        headers: headers(),
        body: JSON.stringify(form)
    });
    const data = await res.json();
    alert(data.message);
});

document.getElementById("statusForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const form = Object.fromEntries(new FormData(e.target).entries());
    const res = await fetch(`${API_BASE}/official/applications/${form.trackingId}/status`, {
        method: "POST",
        headers: headers(),
        body: JSON.stringify({status: form.status, remarks: form.remarks})
    });
    const data = await res.json();
    alert(data.message);
    renderTimeline(form.trackingId);
});

document.getElementById("assignForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const form = Object.fromEntries(new FormData(e.target).entries());
    const res = await fetch(`${API_BASE}/admin/applications/${form.trackingId}/assign`, {
        method: "POST",
        headers: headers(),
        body: JSON.stringify({officialId: form.officialId})
    });
    const data = await res.json();
    alert(data.message);
});

document.getElementById("settingsForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const form = Object.fromEntries(new FormData(e.target).entries());
    const payload = {
        autoApproveDays: Number(form.autoApproveDays),
        delayThresholdHours: Number(form.delayThresholdHours)
    };
    const res = await fetch(`${API_BASE}/admin/settings/auto-approval`, {
        method: "POST",
        headers: headers(),
        body: JSON.stringify(payload)
    });
    const data = await res.json();
    alert(data.message);
});

async function loadDashboard() {
    const res = await fetch(`${API_BASE}/admin/dashboard`, {headers: headers()});
    const data = await res.json();
    document.getElementById("performanceScore").innerText =
        `Avg rating ${data.data.averageRating?.toFixed(2) ?? "N/A"} | Delays ${data.data.delayedApplications}`;
}

async function loadDelays() {
    const res = await fetch(`${API_BASE}/admin/delays`, {headers: headers()});
    const data = await res.json();
    const delays = document.getElementById("delays");
    delays.innerHTML = "";
    data.data.forEach(app => {
        const li = document.createElement("li");
        li.textContent = `${app.trackingId} flagged by AI`;
        delays.appendChild(li);
        state.hashChain.push(app.documentHash);
    });
    document.getElementById("hashChain").textContent = state.hashChain.join(" ⟶ ");
}

function refreshAiEvents() {
    const list = document.getElementById("aiEvents");
    list.innerHTML = "";
    state.aiEvents.slice(-5).forEach(evt => {
        const li = document.createElement("li");
        li.textContent = evt;
        list.appendChild(li);
    });
}

// demo data when backend not running
if (!state.token) {
    document.getElementById("trackResult").textContent = "Login to see live data. Demo populated.";
    document.getElementById("timeline").innerHTML = `
        <li><strong>NEW</strong> - Application submitted</li>
        <li><strong>IN_REVIEW</strong> - Assigned to Fire Dept</li>
        <li><strong>APPROVED</strong> - Document hash anchored</li>`;
    document.getElementById("officialApplications").innerHTML = `
        <li>DG-12345 • Pending review</li>
        <li>DG-56789 • AI flagged for delay</li>`;
    document.getElementById("delays").innerHTML = `
        <li>DG-56789 | 3 days idle</li>`;
    document.getElementById("performanceScore").innerText = "Avg rating 4.8 | Delays 1";
    document.getElementById("hashChain").textContent = "GENESIS ⟶ abcd1234 ⟶ efg45678";
}

loadDashboard();
loadDelays();

