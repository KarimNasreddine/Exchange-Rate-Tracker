import './App.css';
import { useEffect, useState, useCallback } from "react";
import {AppBar, Toolbar, Typography, Button, Snackbar, Alert, TextField, Select, MenuItem } from '@mui/material'
import { DataGrid } from '@mui/x-data-grid';
import UserCredentialsDialog from './UserCredentialsDialog/UserCredentialsDialog';
import { getUserToken,saveUserToken, clearUserToken } from "./localStorage";
import RateCalculator from './RateCalculator/RateCalculator';


var SERVER_URL = "http://127.0.0.1:5000"

function App() {
  let [buyUsdRate, setBuyUsdRate] = useState(null);
  let [sellUsdRate, setSellUsdRate] = useState(null);
  
  let [lbpInput, setLbpInput] = useState("");
  let [usdInput, setUsdInput] = useState("");
  
  let [transactionType, setTransactionType] = useState("usd-to-lbp");

  let [userToken, setUserToken] = useState(getUserToken());

  const States = {
    PENDING: "PENDING",
    USER_CREATION: "USER_CREATION",
    USER_LOG_IN: "USER_LOG_IN",
    USER_AUTHENTICATED: "USER_AUTHENTICATED",
   };

   let [authState, setAuthState] = useState(States.PENDING);

    //used to display the calculations for converting from a currency to another
    let[buy,setBuy] = useState("");
    let[sell,setSell] = useState("");

    let [userTransactions, setUserTransactions] = useState([]);

  function fetchRates() {
    fetch(`${SERVER_URL}/exchangeRate`)
    .then(response => response.json())
    .then(data => {
        setBuyUsdRate(data['lbp_to_usd']);
        setSellUsdRate(data['usd_to_lbp']);
    });
  }

  useEffect(fetchRates, []);

  function addItem() {
    let userLBPValue = lbpInput;
    let userUSDValue = usdInput;
    let usd_to_lbp = transactionType == "usd-to-lbp" ? true : false;
    
    if(userLBPValue != '' && userUSDValue != ''){
      const data = {
          "usd_amount" : userUSDValue,
          "lbp_amount" : userLBPValue,
          "usd_to_lbp" : usd_to_lbp
      };
      
      let header = null; 

      if(userToken==null){
        header = {
            'Content-Type': 'application/json',
        }
      } else{
        header = {
            'Content-Type': 'application/json',
            'Authorization' : 'Bearer ' + userToken
        }
      }

      fetch(`${SERVER_URL}/transaction`, {
        method: 'POST',
        headers: header,
        body: JSON.stringify(data),
      })
      .then(response => response.json())
      .then(data => {
          fetchRates();
          console.log('Success:', data);
      })
      .catch((error) => {
          console.error('Error:', error);
      });
  
      setLbpInput("");
      setUsdInput("");
      setTransactionType("usd-to-lbp");
    }
  }

    function login(username, password) {
        return fetch(`${SERVER_URL}/authentication`, {
            method: "POST",
            headers: {
            "Content-Type": "application/json",
            },
            body: JSON.stringify({
                user_name: username,
                password: password,
            }),
        })
            .then((response) => response.json())
            .then((body) => {
                setAuthState(States.USER_AUTHENTICATED);
                setUserToken(body.token);
                saveUserToken(userToken);
        });
    }

    function createUser(username, password) {
        return fetch(`${SERVER_URL}/user`, {
            method: "POST",
            headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            user_name: username,
            password: password,
        }),
        }).then((response) => login(username, password));
    }
    
    function logout() {
        setUserToken(null);
        clearUserToken();
    }   

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
       

  return (
    <div>
      <html>
        <head>
            <title>LBP Exchange Tracker</title>
            <link rel="stylesheet" href="style.css"/>
        </head>
        <body>
            <AppBar position="static">
                <Toolbar classes={{ root: "nav" }}>
                    <Typography variant="h5">Welcome To Exchanger!</Typography>
                    
                        {
                            userToken !== null ? (
                                <Button color="inherit" onClick={logout}>
                                    Logout
                                </Button>
                            ):(
                                <div> 
                                    <Button color="inherit" onClick={() => setAuthState(States.USER_CREATION)}>
                                        Register
                                    </Button>
                                    <Button color="inherit" onClick={() => setAuthState(States.USER_LOG_IN)}>
                                        Login
                                    </Button>
                                </div>
                            )
                        }
                        
                </Toolbar>
            </AppBar>
            
            <UserCredentialsDialog
                open={authState == States.USER_CREATION}
                onSubmit={(username, password)=>{createUser(username, password)}}
                onClose={()=>{setAuthState(States.PENDING)}}
                title={'User Registration'}
                submitText={'Register'}
            />
            
            <UserCredentialsDialog
                open={authState == States.USER_LOG_IN}
                onSubmit={(username, password)=>{login(username, password)}}
                onClose={()=>{setAuthState(States.PENDING)}}
                title={'User Login'}
                submitText={'Login'}
            />

            <Snackbar
                elevation={6}
                variant="filled"
                open={authState === States.USER_AUTHENTICATED}
                autoHideDuration={2000}
                onClose={() => setAuthState(States.PENDING)}
            >
                <Alert severity="success">Success</Alert>
            </Snackbar>

            <div className="wrapper">
                <Typography variant="h4" gutterBottom>Today's Exchange Rate</Typography>
                <Typography variant="body1">LBP to USD Exchange Rate</Typography>
                
                <Typography variant="h6">Buy USD: 
                  <span id="buy-usd-rate" value={buyUsdRate} onChange={e => setBuyUsdRate(e.target.value)}>
                    {buyUsdRate == null ? "Not Available" : buyUsdRate.toFixed(2).toString()}
                  </span>
                </Typography>
                
                <Typography variant="h6">Sell USD: 
                  <span id="sell-usd-rate" value={sellUsdRate} onChange={e => setSellUsdRate(e.target.value)}>
                    {sellUsdRate == null ? "Not Available" : sellUsdRate.toFixed(2).toString()}
                  </span>
                </Typography>

                <hr />
                
                <RateCalculator 
                    onSubmit={(input,calculationType)=>{
                        if(calculationType==0){
                            setBuy(input*buyUsdRate+" LBP");
                            setSell(input*sellUsdRate+" LBP");
                        }
                        else if(calculationType==1){
                            setBuy(input/buyUsdRate+" USD");
                            setSell(input/sellUsdRate+" USD");
                        }
                    }}
                />
                <Typography variant="h6">Buy:{buy} </Typography>
                <Typography variant="h6">Sell:{sell} </Typography>
                
            </div>

            <div className='wrapper'>
                <Typography variant="h5" gutterBottom>Record A Recent Transaction</Typography>
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

                    <Select 
                        id = "transaction-type" 
                        value={transactionType} 
                        onChange={e=>setTransactionType(e.target.value)}
                    >
                        <MenuItem value="usd-to-lbp">USD to LBP</MenuItem>
                        <MenuItem value="lbp-to-usd">LBP to USD</MenuItem>      
                    </Select> 
                    
                    <div>
                        <Button id = "add-button" className="button" type="button"  variant="outlined" color="primary" onClick={addItem}>Add</Button>
                    </div>
                </form>
                <script src="script.js"></script>
            </div>

            {userToken && (
                <div className="wrapper">
                <Typography variant="h5">Your Transactions</Typography>
                <DataGrid
                    columns={[
                        { field: 'added_date' , headerName:'Date',resizable:true,flex:1}, 
                        { field: 'id' ,headerName:'TxId',flex:1}, 
                        { field: 'lbp_amount' ,headerName:'LBP',flex:1}, 
                        { field: 'usd_amount',headerName:'USD' ,flex:1}, 
                        { field: 'usd_to_lbp' ,headerName:'USD to LBP',flex:1}, 
                        { field: 'user_id', hide:true,flex:1} 
                    ]}
                    rows={userTransactions}
                    autoHeight
                />
                </div>
            )}

        </body>
      </html>
    </div>
  );
}

export default App;
