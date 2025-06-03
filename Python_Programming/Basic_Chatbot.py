# define a function for chatbot
def chatbot():
    while True:
        user_input = input("You: ").lower()

        # if user said hello
        if user_input == "hello":
            print("Bot: Hi!")

        # if user asked how are you
        elif user_input == "how are you":
            print("Bot: I'm fine, thanks!")

        # if user said bye
        elif user_input == "bye":
            print("Bot: Goodbye!")
            break  # exit the loop and stop chatbot
        keep_going = input("Do you want to continue? (yes/no): ").lower()

        # if user says no, stop the chatbot
        if keep_going == "no":
            print("Bot: Okay, goodbye!")
            break
        # reply if input is not known
        else:
            print("Bot: Sorry, I don't understand.")

# call the chatbot function to run it
chatbot()