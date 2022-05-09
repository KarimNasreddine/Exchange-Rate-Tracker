import "./App.css";
import React, { useState } from "react";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Button from "@material-ui/core/Button";
import { Snackbar } from "@material-ui/core";
import UserCredentialsDialog from "./Pages/UserCredentialsDialog/UserCredentialsDialog";
import { Alert } from "@mui/material";
import { getUserToken, saveUserToken, clearUserToken } from "./localStorage";
import logo from "./assets/images/transparent_logo.png";

import {
  Route,
  BrowserRouter as Router,
  Routes,
  Link,
  Outlet,
} from "react-router-dom";
import Home from "./Pages/Home";
import Graph from "./Pages/Graph";
import Listings from "./Pages/Listings";
import Insights from "./Pages/Insights";

var SERVER_URL = "http://127.0.0.1:5000";

function App() {
  let [userToken, setUserToken] = useState(getUserToken());

  const States = {
    PENDING: "PENDING",
    USER_CREATION: "USER_CREATION",
    USER_LOG_IN: "USER_LOG_IN",
    USER_AUTHENTICATED: "USER_AUTHENTICATED",
  };
  let [authState, setAuthState] = useState(States.PENDING);

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
        saveUserToken(body.token);
        refreshPage(); // essential to show transactions table
      });
  }

  function refreshPage() {
    window.location.reload();
  }

  function logout() {
    setUserToken(null);
    clearUserToken();
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

  return (
    <div>
      <html>
        <head>
          <link rel="stylesheet" href="style.css" />
          <title>LBP Exchange Tracker</title>
        </head>
        <body>
          <Router>
            <AppBar position="static">
              <Toolbar classes={{ root: "nav" }}>
                <Link
                  to="/"
                  style={{ textDecoration: "inherit", color: "inherit" }}
                >
                  <img src={logo} style={{ height: "4em" }} alt="currency exchange logo" />
                </Link>
                <div style={{ display: "inline-flex" }}>
                  <Link
                    to="/"
                    style={{ textDecoration: "inherit", color: "inherit" }}
                  >
                    <Button color="inherit">Home</Button>
                  </Link>
                  <Link
                    to="/Graph"
                    style={{ textDecoration: "inherit", color: "inherit" }}
                  >
                    <Button color="inherit">Graph</Button>
                  </Link>
                  <Link
                    to="/Insights"
                    style={{ textDecoration: "inherit", color: "inherit" }}
                  >
                    <Button color="inherit">Insights</Button>
                  </Link>
                  <Outlet />
                  {userToken !== null ? (
                    <div>
                      <Link
                        to="/Listings"
                        style={{ textDecoration: "inherit", color: "inherit" }}
                      >
                        <Button color="inherit">Listings</Button>
                      </Link>
                      <Link
                        to="/"
                        style={{ textDecoration: "inherit", color: "inherit" }}
                      >
                        <Button color="inherit" onClick={logout}>
                          Logout
                        </Button>
                      </Link>
                    </div>
                  ) : (
                    <div>
                      <Button
                        color="inherit"
                        onClick={() => setAuthState(States.USER_CREATION)}
                      >
                        Register
                      </Button>
                      <Button
                        color="inherit"
                        onClick={() => setAuthState(States.USER_LOG_IN)}
                      >
                        Login
                      </Button>
                    </div>
                  )}
                </div>
              </Toolbar>
            </AppBar>
            <UserCredentialsDialog
              open={authState === States.USER_CREATION}
              onSubmit={createUser}
              onClose={() => setAuthState(States.PENDING)}
              title="Enter your credentials"
              submitText="Register"
            />
            <UserCredentialsDialog
              open={authState === States.USER_LOG_IN}
              onSubmit={login}
              onClose={() => setAuthState(States.PENDING)}
              title="Enter your credentials"
              submitText="Login"
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
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/Graph" element={<Graph />} />
              <Route path="/Listings" element={<Listings />} />
              <Route path="/Insights" element={<Insights />} />
              <Route path="*" element={<Home />} />
            </Routes>
          </Router>
          <script src="script.js"></script>
        </body>
      </html>
    </div>
  );
}

export default App;
