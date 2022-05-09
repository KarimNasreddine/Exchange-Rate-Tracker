import { React, useState, useEffect } from "react";
import Typography from "@material-ui/core/Typography";

var SERVER_URL = "http://127.0.0.1:5000";

function Insights() {
  let [sellDate, setsellDate] = useState([]);
  let [sellOpen, setSellOpen] = useState([]);
  let [sellClose, setSellClose] = useState([]);
  let [buyDate, setBuyDate] = useState([]);
  let [buyOpen, setBuyOpen] = useState([]);
  let [buyClose, setBuyClose] = useState([]);
  let [transactionsDate, setTransactionsDate] = useState([]);
  let [transactionsNumber, setTransactionsNumber] = useState([]);
  let [transactionsVolume, setTransactionsVolume] = useState([]);

  function insights() {
    fetch(`${SERVER_URL}/insights`)
      .then((response) => response.json())
      .then((data) => {
        setsellDate(Object.keys(data.usd_to_lbp_open));
        setSellOpen(Object.values(data.usd_to_lbp_open));
        setSellClose(Object.values(data.usd_to_lbp_close));
        setBuyDate(Object.keys(data.lbp_to_usd_open));
        setBuyOpen(Object.values(data.lbp_to_usd_open));
        setBuyClose(Object.values(data.lbp_to_usd_close));
        setTransactionsDate(Object.keys(data.volume_in_trxs));
        setTransactionsNumber(Object.values(data.volume_in_trxs));
        setTransactionsVolume(Object.values(data.volume_in_usd));
      });
  }

  useEffect(insights, []);

  return (
    <div>
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
            Open/Close
          </Typography>
        </div>

        <ul>
          <div
            style={{
              display: "grid",
              gridTemplateColumns: "1fr 1fr",
              placeItems: "center",
            }}
          >
            <div
              className="wrapper"
              style={{
                margin: "auto 0.1em",
                width: "70%",
                textAlign: "center",
                padding: "1em 1.5em",
                height: "97%",
              }}
            >
              <Typography variant="h4" gutterBottom>
                Sell Open-Close
              </Typography>
              {Array.from({ length: sellDate.length }, (_, i) => (
                <h1>
                  <Typography variant="h6">
                    {sellDate[sellDate.length - 1 - i]}:{" "}
                    <span>{sellOpen[sellDate.length - 1 - i]}</span> -{" "}
                    <span>{sellClose[sellDate.length - 1 - i]}</span>
                  </Typography>
                </h1>
              ))}
            </div>
            <div
              className="wrapper"
              style={{
                margin: "auto 0.1em",
                width: "70%",
                textAlign: "center",
                padding: "1em 1.5em",
                height: "97%",
              }}
            >
              <Typography variant="h4" gutterBottom>
                Buy Open-Close
              </Typography>
              {Array.from({ length: buyDate.length }, (_, i) => (
                <h1>
                  <Typography variant="h6">
                    {buyDate[buyDate.length - 1 - i]}:{" "}
                    <span>{buyOpen[buyDate.length - 1 - i]}</span> -{" "}
                    <span>{buyClose[buyDate.length - 1 - i]}</span>
                  </Typography>
                </h1>
              ))}
            </div>
          </div>
        </ul>
      </div>
      <div style={{ margin: "2em" }}>
        <div
          style={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            marginBottom: "1em",
            marginTop: "5em",
          }}
        >
          <Typography variant="h3" gutterBottom>
            Transactions
          </Typography>
        </div>

        <ul>
          <div
            style={{
              display: "grid",
              gridTemplateColumns: "1fr 1fr",
              placeItems: "center",
              marginBottom: "3em",
            }}
          >
            <div
              className="wrapper"
              style={{
                margin: "auto 0.1em",
                width: "70%",
                textAlign: "center",
                padding: "1em 1.5em",
                height: "97%",
              }}
            >
              <Typography variant="h4" gutterBottom>
                Transactions Number
              </Typography>
              {Array.from({ length: transactionsDate.length }, (_, i) => (
                <h1>
                  <Typography variant="h6">
                    {transactionsDate[transactionsDate.length - 1 - i]}:{" "}
                    <span>
                      {transactionsNumber[transactionsDate.length - 1 - i]}
                    </span>
                  </Typography>
                </h1>
              ))}
            </div>
            <div
              className="wrapper"
              style={{
                margin: "auto 0.1em",
                width: "70%",
                textAlign: "center",
                padding: "1em 1.5em",
                height: "97%",
              }}
            >
              <Typography variant="h4" gutterBottom>
                Transactions Volume ($)
              </Typography>
              {Array.from({ length: transactionsDate.length }, (_, i) => (
                <h1>
                  <Typography variant="h6">
                    {transactionsDate[transactionsDate.length - 1 - i]}:{" "}
                    <span>
                      {transactionsVolume[transactionsDate.length - 1 - i]}
                    </span>
                  </Typography>
                </h1>
              ))}
            </div>
          </div>
        </ul>
      </div>
    </div>
  );
}

export default Insights;
