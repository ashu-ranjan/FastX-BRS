// src/Components/Operator/store/store.js

import { configureStore } from "@reduxjs/toolkit";
import { DriverReducer } from "./reducers/DriverReducer";

const store = configureStore({
    reducer: {
        driver: DriverReducer
    }
})

export default store;