import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Form, Button, Alert, Card, Container, Row, Col } from 'react-bootstrap';
import axios from 'axios';

function PaymentPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const { bookingId, totalAmount, journeyDate, origin, destination, busType, selectedSeats } = location.state || {};
  const [paymentMethod, setPaymentMethod] = useState('CREDIT_CARD');
  const [isProcessing, setIsProcessing] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  const handlePayment = async () => {
    if (!bookingId) {
      setError('Invalid booking reference');
      return;
    }

    const paymentRequest = {
      paymentMethod: paymentMethod
    };

    try {
      setIsProcessing(true);
      setError('');
      const response = await axios.post(
        `http://localhost:8080/fastx/api/payment/make-payment?bookingId=${bookingId}`,
        paymentRequest,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`
          }
        }
      );

      if (response.data.paymentStatus === 'SUCCESS') {
        setSuccess(true);
        // Redirect to bookings page after 2 seconds to show success message
        setTimeout(() => {
          navigate('/customer/bookings');
        }, 2000);
      } else {
        setError('Payment failed. Please try again or use a different payment method.');
      }
    } catch (error) {
      console.error('Payment error:', error);
      setError(error.response?.data?.message || 'Payment processing failed. Please try again.');
    } finally {
      setIsProcessing(false);
    }
  };

  if (!bookingId) {
    return (
      <Container className="mt-5">
        <Alert variant="danger">
          No booking information found. Please start the booking process again.
        </Alert>
        <Button variant="primary" onClick={() => navigate('/customer')}>
          Back to Home
        </Button>
      </Container>
    );
  }

  return (
    <Container className="mt-5">
      <Card className="p-4 shadow">
        <h2 className="mb-4 text-center">Complete Your Payment</h2>
        
        {error && <Alert variant="danger">{error}</Alert>}
        {success && <Alert variant="success">Payment successful! Redirecting to your bookings...</Alert>}
        
        <Card className="mb-4 border-primary">
          <Card.Body>
            <h4 className="text-primary">Booking Summary</h4>
            <hr />
            <Row>
              <Col md={6}>
                <p><strong>Booking ID:</strong> {bookingId}</p>
                <p><strong>Journey Date:</strong> {new Date(journeyDate).toLocaleDateString()}</p>
                <p><strong>Route:</strong> {origin} to {destination}</p>
              </Col>
              <Col md={6}>
                <p><strong>Bus Type:</strong> {busType}</p>
                <p><strong>Total Seats:</strong> {selectedSeats?.length || 0}</p>
                <p><strong>Total Amount:</strong> ₹{totalAmount}</p>
              </Col>
            </Row>
            
            {selectedSeats && selectedSeats.length > 0 && (
              <>
                <h5 className="mt-3">Seat Details</h5>
                <ul>
                  {selectedSeats.map((seat, index) => (
                    <li key={index}>
                      Seat {seat.seatNumber} - {seat.passenger}
                    </li>
                  ))}
                </ul>
              </>
            )}
          </Card.Body>
        </Card>

        <Card className="mb-4 border-primary">
          <Card.Body>
            <h4 className="text-primary">Payment Information</h4>
            <hr />
            <Form>
              <Form.Group className="mb-4">
                <Form.Label><h5>Select Payment Method</h5></Form.Label>
                <Form.Select 
                  value={paymentMethod}
                  onChange={(e) => setPaymentMethod(e.target.value)}
                  className="mb-3"
                >
                  <option value="CREDIT_CARD">Credit Card</option>
                  <option value="DEBIT_CARD">Debit Card</option>
                  <option value="NET_BANKING">Net Banking</option>
                  <option value="UPI">UPI</option>
                  <option value="WALLET">Wallet</option>
                </Form.Select>

                {paymentMethod === 'CREDIT_CARD' || paymentMethod === 'DEBIT_CARD' ? (
                  <>
                    <Form.Group className="mb-3">
                      <Form.Label>Card Number</Form.Label>
                      <Form.Control type="text" placeholder="1234 5678 9012 3456" required />
                    </Form.Group>
                    <Row>
                      <Col md={6}>
                        <Form.Group className="mb-3">
                          <Form.Label>Expiry Date</Form.Label>
                          <Form.Control type="text" placeholder="MM/YY" required />
                        </Form.Group>
                      </Col>
                      <Col md={6}>
                        <Form.Group className="mb-3">
                          <Form.Label>CVV</Form.Label>
                          <Form.Control type="text" placeholder="123" required />
                        </Form.Group>
                      </Col>
                    </Row>
                    <Form.Group className="mb-3">
                      <Form.Label>Card Holder Name</Form.Label>
                      <Form.Control type="text" placeholder="John Doe" required />
                    </Form.Group>
                  </>
                ) : paymentMethod === 'UPI' ? (
                  <Form.Group className="mb-3">
                    <Form.Label>UPI ID</Form.Label>
                    <Form.Control type="text" placeholder="name@upi" required />
                  </Form.Group>
                ) : paymentMethod === 'NET_BANKING' ? (
                  <Form.Group className="mb-3">
                    <Form.Label>Select Bank</Form.Label>
                    <Form.Select>
                      <option>State Bank of India</option>
                      <option>HDFC Bank</option>
                      <option>ICICI Bank</option>
                      <option>Axis Bank</option>
                      <option>Punjab National Bank</option>
                    </Form.Select>
                  </Form.Group>
                ) : paymentMethod === 'WALLET' ? (
                  <Form.Group className="mb-3">
                    <Form.Label>Select Wallet</Form.Label>
                    <Form.Select>
                      <option>Paytm</option>
                      <option>PhonePe</option>
                      <option>Amazon Pay</option>
                      <option>MobiKwik</option>
                    </Form.Select>
                  </Form.Group>
                ) : null}
              </Form.Group>

              <div className="d-grid gap-2">
                <Button 
                  variant="primary" 
                  size="lg" 
                  onClick={handlePayment}
                  disabled={isProcessing || success}
                >
                  {isProcessing ? (
                    <>
                      <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                      Processing...
                    </>
                  ) : (
                    `Pay ₹${totalAmount}`
                  )}
                </Button>
              </div>
            </Form>
          </Card.Body>
        </Card>

        <div className="text-center mt-3">
          <Button variant="outline-secondary" onClick={() => navigate(-1)}>
            Back to Booking
          </Button>
        </div>
      </Card>
    </Container>
  );
}

export default PaymentPage;