// Utility: Format date as YYYY-MM-DD
function formatDate(date) {
  return date.toISOString().split('T')[0];
}

// Get elements
const form = document.getElementById('fitness-form');
const todayStepsEl = document.getElementById('today-steps');
const todayWorkoutEl = document.getElementById('today-workout');
const todayCaloriesEl = document.getElementById('today-calories');
const dateInput = document.getElementById('date');

const WEEK_DAYS = 7;
const STORAGE_KEY = 'fitnessData';

// Set date input default to today
dateInput.value = formatDate(new Date());

// Load saved data or empty object
function loadFitnessData() {
  const data = localStorage.getItem(STORAGE_KEY);
  return data ? JSON.parse(data) : {};
}

// Save data back to localStorage
function saveFitnessData(data) {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(data));
}

// Add or update entry
function addEntry(entry) {
  const data = loadFitnessData();
  data[entry.date] = entry;
  saveFitnessData(data);
}

// Calculate totals for a date
function calculateDailyTotals(date) {
  const data = loadFitnessData();
  if (data[date]) {
    return data[date];
  }
  return {
    steps: 0,
    workoutTime: 0,
    calories: 0,
    exerciseType: '',
  };
}

// Update today's summary UI
function updateTodaySummary() {
  const today = formatDate(new Date());
  const todayData = calculateDailyTotals(today);

  todayStepsEl.textContent = todayData.steps || 0;
  todayWorkoutEl.textContent = (todayData.workoutTime || 0) + ' mins';
  todayCaloriesEl.textContent = (todayData.calories || 0) + ' kcal';
}

// Get data for last 7 days for the chart
function getLast7DaysData() {
  const data = loadFitnessData();
  const today = new Date();
  const labels = [];
  const stepsData = [];

  for (let i = WEEK_DAYS - 1; i >= 0; i--) {
    const d = new Date(today);
    d.setDate(d.getDate() - i);
    const dateStr = formatDate(d);
    labels.push(dateStr.slice(5)); // MM-DD
    stepsData.push(data[dateStr]?.steps || 0);
  }
  return { labels, stepsData };
}

// Initialize Chart.js for weekly progress
const ctx = document.getElementById('weeklyChart').getContext('2d');
let weeklyChart;

function renderChart() {
  const { labels, stepsData } = getLast7DaysData();

  if (weeklyChart) {
    weeklyChart.data.labels = labels;
    weeklyChart.data.datasets[0].data = stepsData;
    weeklyChart.update();
  } else {
    weeklyChart = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [
          {
            label: 'Steps',
            data: stepsData,
            backgroundColor: '#3b82f6',
            borderRadius: 6,
          },
        ],
      },
      options: {
        responsive: true,
        scales: {
          y: {
            beginAtZero: true,
            ticks: { stepSize: 1000 },
          },
        },
        plugins: {
          legend: { display: false },
          tooltip: {
            callbacks: {
              label: (context) => `${context.parsed.y} steps`,
            },
          },
        },
      },
    });
  }
}

// Handle form submission
form.addEventListener('submit', (e) => {
  e.preventDefault();

  const date = dateInput.value;
  const steps = parseInt(document.getElementById('steps').value, 10) || 0;
  const workoutTime = parseInt(document.getElementById('workout-time').value, 10) || 0;
  const calories = parseInt(document.getElementById('calories').value, 10) || 0;
  const exerciseType = document.getElementById('exercise-type').value.trim();

  if (!date) {
    alert('Please select a date.');
    return;
  }

  addEntry({ date, steps, workoutTime, calories, exerciseType });

  updateTodaySummary();
  renderChart();

  form.reset();
  dateInput.value = formatDate(new Date());
});

// Initial load
updateTodaySummary();
renderChart();
