const initialState = {
    bus: {},
    driver: {},
    busError: {},
    driverError: {},
    loading: true
};

export const DriverReducer = (state = initialState, action) => {
    switch (action.type) {
        case 'FETCH_BUS':
            return {
                ...state,
                bus: action.payload,
                busError: null,
                loading: !state.driver && state.driver !== null // Only set loading false if driver is loaded or explicitly null
            };
        case 'FETCH_DRIVER':
            return {
                ...state,
                driver: action.payload,
                driverError: null,
                loading: !state.bus // Set loading to false if bus is already loaded
            };
        case 'FETCH_BUS_ERROR':
            return {
                ...state,
                busError: action.payload,
                loading: false
            };
        case 'FETCH_DRIVER_ERROR':
            return {
                ...state,
                driverError: action.payload,
                loading: false
            };
        default:
            return state;
    }
};