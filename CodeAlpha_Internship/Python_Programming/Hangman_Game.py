import random  # for choosing a random word

# List of words to choose from
words = ['apple', 'banana', 'grapes', 'orange', 'mango']

# Randomly select one word from the list
secret_word = random.choice(words)

# List to store letters guessed by the player
guessed_letters = []

# Maximum number of incorrect guesses allowed
tries = 6

print("Welcome to the Hangman Game!")
print("Guess the word by entering one letter at a time.")
print("You have 6 wrong guesses allowed.\n")

# Game loop runs until player runs out of tries or guesses the word
while tries > 0:
    # Build the word display with guessed letters and underscores
    display_word = ''
    for letter in secret_word:
        if letter in guessed_letters:
            display_word += letter + ' '
        else:
            display_word += '_ '

    print("Word:", display_word.strip())

    # If no underscores remain, the word is guessed
    if all(letter in guessed_letters for letter in secret_word):
        print("\nCongratulations! You guessed the word:", secret_word)
        break

    # Get input from player
    guess = input("Enter a letter: ").lower()

    # Check if input is a single alphabet letter or any other than letters like special character or a number
    if len(guess) != 1 or not guess.isalpha():
        print("Please enter one alphabet letter.\n")
        continue

    # Check if the letter was already guessed
    if guess in guessed_letters:
        print("You already guessed that letter.\n")
        continue

    # Add guess to the list
    guessed_letters.append(guess)

    # If guess is correct, continue
    if guess in secret_word:
        print("Correct guess!\n")
    else:
        # If guess is wrong, reduce number of tries
        tries -= 1
        print(f"Wrong guess. Tries left: {tries}\n")

# If tries run out, show the correct word
if tries == 0:
    print("Game Over! The word was:", secret_word)