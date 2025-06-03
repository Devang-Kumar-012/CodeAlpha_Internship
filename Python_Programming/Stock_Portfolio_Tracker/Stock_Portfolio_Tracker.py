import os

# these are some stock prices we already know
stock_prices = {
    "APPLE": 180,
    "TESLA": 250,
    "GOOGLE": 140,
    "AMAZON": 130,
    "MICROSOFT": 300
}

# we'll keep user stock data here
portfolio = {}

# starting to ask user for their stocks
print("Enter your stock details (type 'done' to finish):")

while True:
    # ask for stock name
    stock = input("Stock Symbol: ").upper()

    # if user types done, we stop asking
    if stock == "DONE":
        break

    # check if the stock is available in our list
    if stock in stock_prices:
        # now ask how many they got
        qty = input(f"How many shares of {stock}? ")

        # checking if they typed a number
        if qty.isdigit():
            portfolio[stock] = int(qty)  # save it
        else:
            print("pls enter a valid number, like 5 or 10")
    else:
        print("hmm.. stock not found, try again with valid name")

# now let's print what they entered
print("\nYour Stock Portfolio:")
total_value = 0  # we'll calculate total here

# go through all the stocks user entered
for stock, qty in portfolio.items():
    price = stock_prices[stock]  # get price
    value = price * qty  # total price for that stock
    total_value += value  # add to total
    print(f"{stock}: {qty} shares x ${price} = ${value}")

# print total money invested
print(f"\nTotal Investment Value: ${total_value}")

# ask user if they want to save it to file
save = input("\nSave to file? (yes/no): ").lower()

# if yes, make a file and write stuff
if save == "yes":
    # get the folder path of the current script
    folder_path = os.path.dirname(__file__)
    file_path = os.path.join(folder_path, "portfolio_summary.txt")

    with open(file_path, "w") as f:
        f.write("Your Stock Portfolio:\n")
        for stock, qty in portfolio.items():
            price = stock_prices[stock]
            value = price * qty
            f.write(f"{stock}: {qty} shares x ${price} = ${value}\n")
        f.write(f"\nTotal Investment Value: ${total_value}\n")
    print("Saved to 'portfolio_summary.txt' successfully :)")
