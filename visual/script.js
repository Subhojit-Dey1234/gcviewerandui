const GC_COLORS = {
  YOUNG_NORMAL: "#4CAF50",
  YOUNG_CONCURRENT_START: "#FFC107",
  YOUNG_PREPARE_MIXED: "#FF9800",
  MIXED: "#2196F3",
  FULL: "#F44336"
};


fetch("gc-report.json")
  .then(res => res.json())
  .then(data => {
    renderMeta(data.meta);
    renderSummary(data.summary);
    renderChart(data.events);
    renderTable(data.events);
  });

/* ---------------- META ---------------- */

function renderMeta(meta) {
  document.getElementById("meta").innerText =
    `JVM ${meta.jvm} | GC ${meta.gc} | Heap ${meta.heapMaxMb} MB`;
}

/* ---------------- SUMMARY ---------------- */

function renderSummary(summary) {
  document.getElementById("totalPause").innerText =
    summary.totalGcCount;

  document.getElementById("maxPause").innerText =
    summary.maxPauseMs.toFixed(2);

  document.getElementById("youngCount").innerText =
    summary.youngGcCount;

  document.getElementById("mixedCount").innerText =
    summary.mixedGcCount;
}

/* ---------------- CHART ---------------- */

function renderChart(events) {

  const datasetsByType = {};

  events.forEach(e => {
    if (!datasetsByType[e.type]) {
      datasetsByType[e.type] = {
        label: e.type,
        data: [],
        backgroundColor: GC_COLORS[e.type] || "#999",
        pointRadius: 5,
        showLine: true,
        tension: 0.15
      };
    }

    datasetsByType[e.type].data.push({
      x: e.gcId,
      y: e.pauseMs
    });
  });

  new Chart(document.getElementById("pauseChart"), {
    type: "scatter",
    data: {
      datasets: Object.values(datasetsByType)
    },
    options: {
      plugins: {
        tooltip: {
          callbacks: {
            label: ctx =>
              `${ctx.dataset.label} - ${ctx.parsed.x}: ${ctx.parsed.y.toFixed(2)} ms`
          }
        }
      },
      scales: {
        x: {
          title: {
            display: true,
            text: "GC Cycle Id"
          }
        },
        y: {
          title: {
            display: true,
            text: "GC Pause (ms)"
          }
        }
      }
    }
  });
}

/* ---------------- TABLE ---------------- */

function renderTable(events) {
  const tbody = document.querySelector("#gcTable tbody");
  tbody.innerHTML = "";

  events.forEach(e => {
    const row = document.createElement("tr");

    row.innerHTML = `
      <td>${e.gcId}</td>
      <td>${(e.uptimeMs / 1000).toFixed(2)} s</td>
      <td style="color:${GC_COLORS[e.type] || "#000"}">${e.type}</td>
      <td>${bytesToMb(e.heapBeforeInBytes)} MB</td>
      <td>${bytesToMb(e.heapAfterInBytes)} MB</td>
      <td>${e.pauseMs.toFixed(2)}</td>
    `;

    tbody.appendChild(row);
  });
}

/* ---------------- UTILS ---------------- */

function bytesToMb(bytes) {
  return (bytes / (1024 * 1024)).toFixed(1);
}
