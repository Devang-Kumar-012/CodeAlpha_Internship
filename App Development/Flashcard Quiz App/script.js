// Select DOM elements
const flashcard = document.getElementById("flashcard");
const frontContent = flashcard.querySelector(".front");
const backContent = flashcard.querySelector(".back");
const toggleAnswerBtn = document.getElementById("toggle-answer");

const prevBtn = document.getElementById("prev");
const nextBtn = document.getElementById("next");
const cardIndexDisplay = document.getElementById("card-index");

const flashcardForm = document.getElementById("flashcard-form");
const questionInput = document.getElementById("question-input");
const answerInput = document.getElementById("answer-input");

const flashcardList = document.getElementById("flashcard-list");

let flashcards = JSON.parse(localStorage.getItem("flashcards")) || [
  { question: "What is JavaScript?", answer: "A programming language used for web development." },
  { question: "What does HTML stand for?", answer: "HyperText Markup Language." },
];

let currentIndex = 0;
let showingAnswer = false;

function renderFlashcard() {
  if (flashcards.length === 0) {
    frontContent.textContent = "No flashcards available.";
    backContent.textContent = "";
    toggleAnswerBtn.style.display = "none";
    cardIndexDisplay.textContent = "0 / 0";
    return;
  }

  toggleAnswerBtn.style.display = "inline-block";

  let card = flashcards[currentIndex];
  frontContent.textContent = card.question;
  backContent.textContent = card.answer;
  backContent.classList.add("hidden");
  frontContent.classList.remove("hidden");
  toggleAnswerBtn.textContent = "Show Answer";

  cardIndexDisplay.textContent = `${currentIndex + 1} / ${flashcards.length}`;
  showingAnswer = false;
}

function toggleAnswer() {
  if (showingAnswer) {
    backContent.classList.add("hidden");
    frontContent.classList.remove("hidden");
    toggleAnswerBtn.textContent = "Show Answer";
  } else {
    backContent.classList.remove("hidden");
    frontContent.classList.add("hidden");
    toggleAnswerBtn.textContent = "Hide Answer";
  }
  showingAnswer = !showingAnswer;
}

function nextCard() {
  if (flashcards.length === 0) return;
  currentIndex = (currentIndex + 1) % flashcards.length;
  renderFlashcard();
}

function prevCard() {
  if (flashcards.length === 0) return;
  currentIndex = (currentIndex - 1 + flashcards.length) % flashcards.length;
  renderFlashcard();
}

function saveFlashcards() {
  localStorage.setItem("flashcards", JSON.stringify(flashcards));
}

function renderFlashcardList() {
  flashcardList.innerHTML = "";
  flashcards.forEach((card, idx) => {
    const li = document.createElement("li");
    li.classList.add("flashcard-item");

    const textDiv = document.createElement("div");
    textDiv.className = "text";
    textDiv.textContent = card.question;

    // Edit button
    const editBtn = document.createElement("button");
    editBtn.textContent = "Edit";
    editBtn.style.backgroundColor = "#007bff";
    editBtn.style.marginRight = "8px";
    editBtn.onclick = () => editFlashcard(idx);

    // Delete button
    const deleteBtn = document.createElement("button");
    deleteBtn.textContent = "Delete";
    deleteBtn.onclick = () => deleteFlashcard(idx);

    li.appendChild(textDiv);
    li.appendChild(editBtn);
    li.appendChild(deleteBtn);

    flashcardList.appendChild(li);
  });
}

function addFlashcard(question, answer) {
  flashcards.push({ question, answer });
  saveFlashcards();
  renderFlashcardList();
  renderFlashcard();
}

function deleteFlashcard(index) {
  if (index === currentIndex) {
    currentIndex = 0;
  }
  flashcards.splice(index, 1);
  if (currentIndex >= flashcards.length) {
    currentIndex = flashcards.length - 1;
  }
  saveFlashcards();
  renderFlashcardList();
  renderFlashcard();
}

function editFlashcard(index) {
  const card = flashcards[index];
  questionInput.value = card.question;
  answerInput.value = card.answer;

  flashcardForm.querySelector("button").textContent = "Update Flashcard";

  flashcardForm.onsubmit = (e) => {
    e.preventDefault();
    flashcards[index] = {
      question: questionInput.value.trim(),
      answer: answerInput.value.trim(),
    };
    questionInput.value = "";
    answerInput.value = "";
    flashcardForm.querySelector("button").textContent = "Add Flashcard";
    flashcardForm.onsubmit = handleAddSubmit;

    saveFlashcards();
    renderFlashcardList();
    renderFlashcard();
  };
}

function handleAddSubmit(e) {
  e.preventDefault();
  const question = questionInput.value.trim();
  const answer = answerInput.value.trim();

  if (!question || !answer) return alert("Please enter both question and answer.");

  addFlashcard(question, answer);
  questionInput.value = "";
  answerInput.value = "";
}

toggleAnswerBtn.addEventListener("click", toggleAnswer);
nextBtn.addEventListener("click", nextCard);
prevBtn.addEventListener("click", prevCard);
flashcardForm.addEventListener("submit", handleAddSubmit);

// Initialize app
renderFlashcard();
renderFlashcardList();
