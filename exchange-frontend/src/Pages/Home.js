import "../App.css";
import React, { useState, useEffect, useCallback } from "react";
import Button from "@material-ui/core/Button";
import { Typography, TextField, MenuItem } from "@material-ui/core";
import { getUserToken } from "../localStorage";
import { DataGrid } from "@mui/x-data-grid";
import Select from "@mui/material/Select";

var SERVER_URL = "http://127.0.0.1:5000";

const dataGridColumns = [
  {
    field: "added_date",
    headerName: "Date",
    resizable: true,
    flex: 1,
  },
  { field: "id", headerName: "TxId", flex: 1 },
  {
    field: "lbp_amount",
    headerName: "LBP",
    flex: 1,
  },
  {
    field: "usd_amount",
    headerName: "USD",
    flex: 1,
  },
  {
    field: "usd_to_lbp",
    headerName: "USD to LBP",
    flex: 1,
  },
  { field: "user_id", hide: true, flex: 1 },
];

function Home() {
  let [buyUsdRate, setBuyUsdRate] = useState(null);
  let [sellUsdRate, setSellUsdRate] = useState(null);
  let [lbpInput, setLbpInput] = useState("");
  let [usdInput, setUsdInput] = useState("");
  let [transactionType, setTransactionType] = useState("usd-to-lbp");
  let [userToken, setUserToken] = useState(getUserToken());
  let [lbpConversion, setLbpConversion] = useState(0);
  let [usdConversion, setUsdConversion] = useState(0);
  let [userTransactions, setUserTransactions] = useState([]);

  function fetchRates() {
    fetch(`${SERVER_URL}/exchangeRate`)
      .then((response) => response.json())
      .then((data) => {
        // console.log(data); // for testing purposes
        setBuyUsdRate(
          Math.round((data.lbp_to_usd + Number.EPSILON) * 100) / 100
        );
        setSellUsdRate(
          Math.round((data.usd_to_lbp + Number.EPSILON) * 100) / 100
        );
      });
  }

  useEffect(fetchRates, []);

  const fetchUserTransactions = useCallback(() => {
    fetch(`${SERVER_URL}/transaction`, {
      headers: {
        Authorization: `bearer ${userToken}`,
      },
    })
      .then((response) => response.json())
      .then((transactions) => setUserTransactions(transactions));
  }, [userToken]);

  useEffect(() => {
    if (userToken) {
      fetchUserTransactions();
    }
  }, [fetchUserTransactions, userToken]);

  function addItem() {
    let headers = { "Content-Type": "application/json" };
    if (userToken) {
      headers["Authorization"] = `Bearer ${userToken}`;
    }
    fetch(`${SERVER_URL}/transaction`, {
      method: "POST",
      headers: headers,
      body: JSON.stringify({
        usd_amount: usdInput,
        lbp_amount: lbpInput,
        usd_to_lbp: transactionType === "usd-to-lbp" ? true : false,
      }),
    })
      .then((response) => response.json())
      .then((data) => {
        // console.log(data.message); // for testing purposes
        setLbpInput("");
        setUsdInput("");
        fetchRates();
      })
      .then(() => {
        fetchRates();
        fetchUserTransactions();
      });
    fetchRates();
    fetchUserTransactions();
  }

  function buyUsdRateFunction() {
    if (buyUsdRate == null) {
      return "Not Available";
    } else {
      return buyUsdRate;
    }
  }

  function sellUsdRateFunction() {
    if (sellUsdRate == null) {
      return "Not Available";
    } else {
      return sellUsdRate;
    }
  }

  function convertToUSD(value) {
    if (value == null) {
      setUsdConversion("");
    } else {
      setUsdConversion((value / buyUsdRate).toFixed(2));
    }
  }

  function convertFromUSD(value) {
    if (value == null) {
      setLbpConversion("");
    } else {
      setLbpConversion(value * sellUsdRate);
    }
  }

  return (
    <div>
      <head>
        <link rel="stylesheet" href="style.css" />
        <title>LBP Exchange Tracker</title>
      </head>
      <body>
        <div className="wrapper">
          <Typography variant="h4" gutterBottom>
            Today's Exchange Rate
          </Typography>
          <Typography variant="body1">LBP to USD Exchange Rate</Typography>
          <Typography variant="h6">
            Buy USD: <span id="buy-usd-rate">{buyUsdRateFunction()}</span>
          </Typography>
          <Typography variant="h6">
            Sell USD: <span id="sell-usd-rate">{sellUsdRateFunction()}</span>
          </Typography>
          <hr />
          <Typography variant="h5" gutterBottom>
            Calculate conversion between USD and LBP
          </Typography>
          <form name="transaction-entry">
            <div className="amount-input">
              <div className="conversionDiv">
                <TextField
                  id="lbp-amount"
                  label="LBP Amount"
                  type="number"
                  size="Normal"
                  margin="dense"
                  onChange={(e) => convertToUSD(e.target.value)}
                />
                <Typography variant="h6" className="conversionText">
                  USD : {usdConversion}
                </Typography>
              </div>
              <div className="conversionDiv">
                <TextField
                  id="usd-amount"
                  label="USD Amount"
                  type="number"
                  margin="dense"
                  onChange={(e) => convertFromUSD(e.target.value)}
                />
                <Typography variant="h6" className="conversionText">
                  LBP : {lbpConversion}
                </Typography>
              </div>
            </div>
          </form>
        </div>
        <div className="wrapper">
          <Typography variant="h5" gutterBottom>
            Record a recent transaction
          </Typography>
          <form name="transaction-entry">
            <div className="amount-input">
              <TextField
                id="lbp-amount"
                label="LBP Amount"
                type="number"
                value={lbpInput}
                onChange={({ target: { value } }) => setLbpInput(value)}
              />
            </div>
            <div className="amount-input">
              <TextField
                id="usd-amount"
                label="USD Amount"
                type="number"
                value={usdInput}
                onChange={({ target: { value } }) => setUsdInput(value)}
              />
            </div>
            <div className="button-container">
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
                onClick={addItem}
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
        {userToken && (
          <div className="wrapper" style={{ marginBottom: "3em" }}>
            <Typography variant="h5" className="tableTitle">
              Your Transactions
            </Typography>
            <DataGrid
              columns={dataGridColumns}
              rows={userTransactions}
              autoHeight
            />
          </div>
        )}
        <script src="script.js"></script>
      </body>
    </div>
  );
}

export default Home;
