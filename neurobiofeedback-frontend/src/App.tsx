import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import {Navbar} from "./layouts";
import {BookingForm, Bookings} from "./components";
import {ChangePassword, ChangeWorkingTime, DefaultBookingData, Login, Settings, Signup} from "./pages";
import {AuthProvider, LanguageProvider, ThemeProvider} from "./context";
import DeleteAccount from "./pages/DeleteAccount.tsx";

function App() {
    return (
        <Router>
            <LanguageProvider>
                <ThemeProvider>
                    <AuthProvider>
                        <div className="flex flex-col min-h-screen">
                        <Navbar/>
                            <Routes>
                                <Route path="/" element={
                                    <div className="flex-grow flex items-center justify-center">
                                        <BookingForm />
                                    </div>
                                }/>
                                <Route path="/bookings" element={
                                    <div className="flex-grow flex items-start p-4 justify-center">
                                        <Bookings/>
                                    </div>
                                }/>
                                <Route path="/add-booking" element={
                                    <div className="flex-grow flex items-center justify-center">
                                        <BookingForm />
                                    </div>
                                } />
                                <Route path="/sign-up" element={
                                    <div className="flex-grow flex items-center justify-center">
                                        <Signup />
                                    </div>
                                } />
                                <Route path="/sign-in" element={
                                    <div className="flex-grow flex items-center justify-center">
                                        <Login />
                                    </div>
                                } />
                                <Route path="/settings" element={
                                    <div className="flex-grow flex items-center justify-center">
                                        <Settings />
                                    </div>
                                } />
                                <Route path="/working-time" element={
                                    <div className="flex-grow flex items-center justify-center">
                                        <ChangeWorkingTime />
                                    </div>
                                } />
                                <Route path="/default-booking-data" element={
                                    <div className="flex-grow flex items-center justify-center">
                                        <DefaultBookingData />
                                    </div>
                                } />
                                <Route path="/change-password" element={
                                    <div className="flex-grow flex items-center justify-center">
                                        <ChangePassword />
                                    </div>
                                } />
                                <Route path="/delete-account" element={
                                    <div className="flex-grow flex items-center justify-center">
                                        <DeleteAccount />
                                    </div>
                                } />
                            </Routes>
                        </div>
                    </AuthProvider>
                </ThemeProvider>
            </LanguageProvider>
        </Router>
    );
}

export default App;
