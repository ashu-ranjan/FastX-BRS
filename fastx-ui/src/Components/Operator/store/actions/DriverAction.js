import axios from "axios";

export const fetchBusDetails = (busId) => {
    return (dispatch) => {
        axios.get(`http://localhost:8080/fastx/api/bus/get-bus/${busId}`, {
            headers: {
                Authorization: `Bearer ${localStorage.getItem("token")}`
            }
        })
            .then(response => {
                dispatch({
                    type: 'FETCH_BUS',
                    payload: response.data
                });
            })
            .catch(error => {
                console.error("Error fetching bus details:", error);
                dispatch({
                    type: 'FETCH_BUS_ERROR',
                    payload: error.message
                });
            });
    };
};

export const fetchDriverDetails = (busId) => {
    return (dispatch) => {
        axios.get(`http://localhost:8080/fastx/api/driver/bus/${busId}`, {
            headers: {
                Authorization: `Bearer ${localStorage.getItem("token")}`
            }
        })
            .then(response => {
                dispatch({
                    type: 'FETCH_DRIVER',
                    payload: response.data || null
                });
            })
            .catch(error => {
                // Handle 404 or 400 (no driver found) by setting driver to null
                if (error.response && (error.response.status === 404 || error.response.status === 400)) {
                    dispatch({
                        type: 'FETCH_DRIVER',
                        payload: null // Explicitly set driver to null
                    });
                } else {
                    console.error("Error fetching driver details:", error);
                    dispatch({
                        type: 'FETCH_DRIVER_ERROR',
                        payload: error.message
                    });
                }
            });
    };
};

export const fetchBusAndDriverDetails = (busId) => {
    return async (dispatch) => {
        try {
            await dispatch(fetchBusDetails(busId));
            await dispatch(fetchDriverDetails(busId));
        } catch (error) {
            console.error("Error fetching bus and driver details:", error);
        }
    };
};