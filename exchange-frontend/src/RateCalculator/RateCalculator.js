import { Typography, TextField, Button } from "@mui/material";
import CheckIcon from '@material-ui/icons/Check';
import FlipCameraAndroidIcon from '@material-ui/icons/FlipCameraAndroid';
import React, { useState } from "react";
import "./RateCalculator.css";

// Component that presents a block to perform exchange values of specific amounts of money
export default function RateCalculator({
    onSubmit,
}) {
    let [userInput,setInput] = useState(null);
    let[calculationType,setCalculationType] = useState(0);
    let[conversionFrom,setConverstionFrom] = useState("USD");
    let[conversionTo,setConverstionTo] = useState("LBP");
    return (
        <div className="conversion-container">
            <div className="calculator-title">
                <Typography variant="h5">
                    Exchanger Currency Converter
                </Typography>
            </div>
            
            <div className="calculator-input">
                <TextField 
                    id="inputCalc"
                    type="number"
                    helperText="Amount"
                    color="primary"
                    margin="normal"
                    value={userInput}
                    onChange={({ target: { value } }) => setInput(value)} 
                />
            </div>

            <div className="calculation-type">
                <Button id = "from" variant = "outlined" color="primary">{conversionFrom}</Button>
                <span>  To  </span>
                <Button id = "to" variant = "outlined" color="primary">{conversionTo}</Button>
            </div>

            <div>
                <div className="button-actions">
                    <FlipCameraAndroidIcon color = "primary" style={{ fontSize: 40 }} onClick= {()=> {
                        let temp = conversionFrom;
                        setConverstionFrom(conversionTo);
                        setConverstionTo(temp);
                        setCalculationType((calculationType+1)%2);
                }}/>
                </div>
                <div className="button-actions">
                    <CheckIcon color = "primary" style={{ fontSize: 40 }} onClick= {()=> {
                        onSubmit(userInput,calculationType);
                    }}/>
                </div>
            </div>  

        </div>

    );
}