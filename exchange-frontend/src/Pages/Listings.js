import { React, useState, useEffect } from "react";
import Typography from "@material-ui/core/Typography";
import { TextField, Select, MenuItem, Button } from "@material-ui/core";
import { getUserToken } from "../localStorage";

var SERVER_URL = "http://127.0.0.1:5000";

function Listings() {
  let [sellAmount, setSellAmount] = useState();
  let [buyAskAmount, setBuyAskAmount] = useState();
  let [phoneNumber, setPhoneNumber] = useState();
  let [transactionType, setTransactionType] = useState("usd-to-lbp");

  let [sellAmountListings, setSellAmountListings] = useState([]);
  let [buyAskAmountListings, setBuyAskAmountListings] = useState([]);
  let [phoneNumberLisitings, setPhoneNumberLisitings] = useState([]);
  let [transactionTypeListings, setTransactionTypeListings] = useState([]);

  let [userToken, setUserToken] = useState(getUserToken());

  function addListing() {
    let headers = { "Content-Type": "application/json" };
    if (userToken) {
      headers["Authorization"] = `Bearer ${userToken}`;
    }
    fetch(`${SERVER_URL}/listing`, {
      method: "POST",
      headers: headers,
      body: JSON.stringify({
        user_phone_number: phoneNumber,
        selling_amount: sellAmount,
        buying_amount: buyAskAmount,
        usd_to_lbp: transactionType === "usd-to-lbp" ? true : false,
      }),
    })
      .then((response) => response.json())
      .then((data) => {
        setSellAmount("");
        setBuyAskAmount("");
        setPhoneNumber("");
        listings();
      });
  }

  function listings() {
    fetch(`${SERVER_URL}/listings`)
      .then((response) => response.json())
      .then((data) => {
        setSellAmountListings([]);
        setBuyAskAmountListings([]);
        setPhoneNumberLisitings([]);
        setTransactionTypeListings([]);
        for (let i = data.length - 1; i >= 0; i--) {
          setSellAmountListings((sellAmountListings) => [
            ...sellAmountListings,
            data[i].selling_amount,
          ]);
          setBuyAskAmountListings((buyAskAmountListings) => [
            ...buyAskAmountListings,
            data[i].buying_amount,
          ]);
          setPhoneNumberLisitings((phoneNumberLisitings) => [
            ...phoneNumberLisitings,
            data[i].user_phone_number,
          ]);
          setTransactionTypeListings((transactionTypeListings) => [
            ...transactionTypeListings,
            data[i].usd_to_lbp,
          ]);
        }
      });
  }

  useEffect(listings, []);

  return (
    <div style={{ margin: "2em" }}>
      <div
        style={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          marginBottom: "1em",
        }}
      >
        <Typography variant="h3" gutterBottom>
          Listings
        </Typography>
      </div>
      <div
        className="wrapper"
        style={{
          textAlign: "center",
          width: "21%",
        }}
      >
        <Typography variant="h5" gutterBottom>
          Add Listing
        </Typography>
        <form name="transaction-entry">
          <div className="amount-input">
            <TextField
              id="sell-amount"
              label="Sell Amount"
              type="number"
              value={sellAmount}
              onChange={({ target: { value } }) => setSellAmount(value)}
              style={{ width: "100%" }}
            />
          </div>
          <div className="amount-input">
            <TextField
              id="buy-ask-amount"
              label="Buy Ask Amount"
              type="number"
              value={buyAskAmount}
              onChange={({ target: { value } }) => setBuyAskAmount(value)}
              style={{ width: "100%" }}
            />
          </div>
          <div className="amount-input">
            <TextField
              id="phone-number"
              label="Phone Number"
              type="number"
              value={phoneNumber}
              onChange={({ target: { value } }) => setPhoneNumber(value)}
              style={{ width: "100%" }}
            />
          </div>
          <div style={{ paddingTop: "1em", textAlign: "left" }}>
            <Select
              id="transaction-type"
              defaultValue={transactionType}
              size="small"
              onChange={({ target: { value } }) => setTransactionType(value)}
            >
              <MenuItem value="usd-to-lbp">USD to LBP</MenuItem>
              <MenuItem value="lbp-to-usd">LBP to USD</MenuItem>
            </Select>
            <Button
              id="add-button"
              className="button"
              type="button"
              onClick={addListing}
              size="medium"
              style={{ marginLeft: "10px" }}
              color="primary"
              variant="outlined"
            >
              Add
            </Button>
          </div>
        </form>
      </div>
      <ul style={{ padding: "0" }}>
        <div
          style={{
            display: "grid",
            gridTemplateColumns: "repeat(3, auto)",
            placeItems: "center",
          }}
        >
          {Array.from({ length: transactionTypeListings.length }, (_, i) => (
            <h1>
              <Typography variant="h6">
                <h1>
                  <div
                    className="wrapper"
                    style={{
                      margin: "auto 0.1em",
                      width: "fit-content",
                      textAlign: "center",
                      padding: "1em 1.5em",
                    }}
                  >
                    <Typography variant="h4" gutterBottom>
                      Listing: {i + 1}
                    </Typography>
                    <div style={{ marginLeft: "0em" }}>
                      <Typography variant="h6">
                        Amount Selling: <span>{sellAmountListings[i]}</span>{" "}
                        {transactionTypeListings[i] === true ? (
                          <span>USD</span>
                        ) : (
                          <span>LBP</span>
                        )}
                      </Typography>
                      <Typography variant="h6">
                        Buying Amount Ask:{" "}
                        <span>{buyAskAmountListings[i]}</span>{" "}
                        {transactionTypeListings[i] === true ? (
                          <span>LBP</span>
                        ) : (
                          <span>USD</span>
                        )}
                      </Typography>
                      <Typography variant="h6">
                        Listing Type:{" "}
                        <span>
                          {transactionTypeListings[i] === true ? (
                            <span>USD to LBP</span>
                          ) : (
                            <span>LBP to USD</span>
                          )}
                        </span>
                      </Typography>
                      <Typography variant="h6">
                        Phone Number: <span>{phoneNumberLisitings[i]}</span>
                      </Typography>
                    </div>
                  </div>
                </h1>
              </Typography>
            </h1>
          ))}
        </div>
      </ul>
    </div>
  );
}
export default Listings;
