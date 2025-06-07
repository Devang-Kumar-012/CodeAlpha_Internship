import nltk
import string
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

nltk.download('punkt')
nltk.download('stopwords')

# Your FAQ data
faqs = {
    "What is your return policy?": "You can return any product within 30 days of purchase.",
    "How can I track my order?": "Track your order using the ID sent to your email.",
    "Do you offer international shipping?": "Yes, to selected countries.",
    "How do I cancel my order?": "Go to 'My Orders' and click on cancel.",
    "What payment methods are accepted?": "We accept cards and UPI."
}

# Preprocess function
def preprocess(text):
    tokens = nltk.word_tokenize(text.lower())
    stop_words = nltk.corpus.stopwords.words('english')
    tokens = [word for word in tokens if word not in string.punctuation and word not in stop_words]
    return ' '.join(tokens)

# Prepare data
faq_questions = list(faqs.keys())
preprocessed_questions = [preprocess(q) for q in faq_questions]
vectorizer = TfidfVectorizer()
faq_vectors = vectorizer.fit_transform(preprocessed_questions)

# Chatbot function
def get_best_answer(user_input):
    user_input_processed = preprocess(user_input)
    user_vector = vectorizer.transform([user_input_processed])
    similarities = cosine_similarity(user_vector, faq_vectors)
    max_index = similarities.argmax()
    confidence = similarities[0][max_index]
    
    if confidence < 0.3:
        return "Sorry, I don't understand your question."
    
    return faqs[faq_questions[max_index]]

# Chat loop
print("Welcome to the FAQ Chatbot! Type 'exit' to stop.")
while True:
    user_input = input("You: ")
    if user_input.lower() == "exit":
        break
    response = get_best_answer(user_input)
    print("Bot:", response)
