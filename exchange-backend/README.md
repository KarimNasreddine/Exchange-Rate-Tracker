# Backend

## About
Developed by Abed Karim Nasreddine.

## Technologies and Libraries

- The project was built using IntelliJ IDEA
- Flask : A micro-framework for Python. It allows you to build websites and web apps quite rapidly and easily.
- MySQL : A relational database management system based on SQL – Structured Query Language. The application is used for a wide range of purposes, including data warehousing, e-commerce, and logging applications.

## Setup

### Step 1 : Backend Setup

Follow the steps below to setup the backend:

1. Clone the repo:
1. Create a new folder anywhere on your PC.
1. Open CMD or Terminal and change the path to the new folder created.
1. Write : git clone https://github.com/maarouf-yassine/Exchange-Rate-Tracker
1. Install Flask:
1. Inside the repo directory, initialize a virtual enviroment. Type:
   - `python3 -m venv venv` on MacOS or Linux.
   - `py -3 -m venv venv` on Windows
1. Activate the virtual enviroment.
   - `venv/bin/activate` on MacOS or Linux
   - `venv\Scripts\activate` on Windows
1. Install flask: `pip install Flask`
1. To install the dependencies needed to run the application, run `pip install -r requirements.txt`.
1. Make sure MySQL is installed on your machine. Visit: https://dev.mysql.com/doc/mysql-installation-excerpt/5.7/en/
1. Create a file at the top level named db_config.py, and one line to it:

   - `DB_CONFIG = 'mysql+pymysql://{mysql_username}:{mysql_password}@{mysql_host}:{mysql_port}/{mysql_db_name}'`
     - For example: `DB_CONFIG = 'mysql+pymysql://root:root@127.0.0.1:3306/exchange'`

1. To be able to access the built-in OpenAPI documentation, run `pip install flasgger`.

### Step 2 : Run The Backend

1. To run the flask application, write:
   1. `set FLASK_APP=app.py`
   2. `python run.py`
2. To access the documentation, visit `{Flask Server URL}:5000/apidocs`.

## Functionality Delivered

1. User can sign up, login, logout, or use the platform as a guest user.

2. Check The Exchange Rate for Buying USD and Selling USD based on the transactions of last 3 days

3. Adding transactions USD -> LBP or LBP -> USD. May be done by an authenticated user or a guest user.

4. Converting a given amount of money from USD to LBP or LBP to USD. Calculation is based on the exchange rate.

5. Viewing user-specific transactions in a table.

6. Providing a platform for users to perform exchange transactions amongst each other. Users can list a transaction with their phone number to be contacted on in order to complete transaction between 2 users. Users could also see all transaction listings posted.

7. Viewing a graph that shows the fluctuation of the exchange rate over time.

8. Viewing some insights and statistics such as Open and Close Values of buy and sell rates for each day in the past 10 days. In addition to viewing the number of transactions performed every day and the volume of these transactions in Dollars.

## Database Models

The database has 3 models:

- User
  - Attributes :
    - id : Primary Key
    - user_name
    - hashed_password
- Transaction
  - Attributes :
    - id : Primary Key
    - usd_amount
    - lbp_amount
    - usd_to_lbp
    - added_date
    - user_id : Foreign Key to User(id)
- Listing
  - Attributes :
    - listing_id : Primary Key
    - posting_user_id : Foreign Key to User(user_name)
    - user_phone_number
    - selling_amount
    - buying_amount
    - usd_to_lbp
    - resolved
    - resolved_by_user

## Project Structure

![Images/Img.PNG](Images/Img.PNG)
