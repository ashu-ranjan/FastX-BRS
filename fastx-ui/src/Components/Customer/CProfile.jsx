import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import { Form, Button, Card, Image, Alert, Row, Col } from 'react-bootstrap';
import { FaUser, FaSave, FaEdit } from 'react-icons/fa';
import "../Customer/css/CProfile.css";

function CProfile() {
  const { customerId } = useParams();
  const [cacheBuster, setCacheBuster] = useState(Date.now());

  const [customer, setCustomer] = useState({
    name: '',
    email: '',
    contact: '',
    address: '',
    gender: 'MALE',
    profilePic: ''
  });

  const [editMode, setEditMode] = useState(false);
  const [loading, setLoading] = useState(false);
  const [imageLoading, setImageLoading] = useState(false);
  const [message, setMessage] = useState({ text: '', type: '' });
  const [selectedFile, setSelectedFile] = useState(null);
  const [previewUrl, setPreviewUrl] = useState(null);

  // ✅ Define reusable function to fetch updated profile
  const refreshCustomerProfile = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/fastx/api/customer/get/profile`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`
        }
      });
      setCustomer(response.data);
      setCacheBuster(Date.now()); // force image refresh
    } catch (error) {
      setMessage({ text: 'Failed to refresh profile', type: 'danger' });
    }
  };

  // Initial load
  useEffect(() => {
    refreshCustomerProfile();
  }, [customerId]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setCustomer(prev => ({ ...prev, [name]: value }));
  };

  const handleFileChange = (e) => {
    if (e.target.files[0]) {
      const file = e.target.files[0];
      setSelectedFile(file);

      const reader = new FileReader();
      reader.onload = (event) => {
        setPreviewUrl(event.target.result);
      };
      reader.readAsDataURL(file);
    }
  };

  const handleProfileUpdate = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage({ text: '', type: '' });

    try {
      const { profilePic, ...profileData } = customer;
      await axios.put(
        `http://localhost:8080/fastx/api/customer/update/profile`,
        profileData,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`
          }
        }
      );
      setEditMode(false);
      await refreshCustomerProfile(); // ✅ Refresh after update
    } catch (error) {
      setMessage({
        text: error.response?.data?.message || 'Failed to update profile',
        type: 'danger'
      });
    } finally {
      setLoading(false);
    }
  };

  const handleImageUpload = async () => {
    if (!selectedFile) return;

    setImageLoading(true);
    setMessage({ text: '', type: '' });

    try {
      const formData = new FormData();
      formData.append('file', selectedFile);

      await axios.post(
        `http://localhost:8080/fastx/api/customer/upload/image`,
        formData,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`
          }
        }
      );

      setPreviewUrl(null);
      setSelectedFile(null);
      setMessage({ text: 'Profile picture updated successfully', type: 'success' });
      await refreshCustomerProfile(); // ✅ Refresh after upload
    } catch (error) {
      setMessage({
        text: error.response?.data?.message || 'Failed to upload image',
        type: 'danger'
      });
    } finally {
      setImageLoading(false);
    }
  };

  const getProfileImageSrc = () => {
    if (previewUrl) return previewUrl;
    if (customer.profilePic) return `/images/${customer.profilePic}?t=${cacheBuster}`;
    return null;
  };

  return (
    <div className="profile-container">
      <Card className="profile-card">
        <Card.Header className="profile-header">
          <h3>My Profile</h3>
        </Card.Header>

        <Card.Body>
          {message.text && <Alert variant={message.type}>{message.text}</Alert>}

          {editMode ? (
            <Form onSubmit={handleProfileUpdate}>
              <Row>
                <Col md={4} className="text-center">
                  <div className="profile-image-container">
                    {getProfileImageSrc() ? (
                      <Image
                        src={getProfileImageSrc()}
                        roundedCircle
                        className="profile-image"
                      />
                    ) : (
                      <div className="profile-image-placeholder">
                        <FaUser size={50} />
                      </div>
                    )}
                    <Form.Group className="mt-3">
                      <Form.Label className="btn btn-outline-primary btn-sm">
                        Change Photo
                        <Form.Control
                          id="profileImage"
                          type="file"
                          onChange={handleFileChange}
                          accept="image/*"
                          className="d-none"
                        />
                      </Form.Label>
                      {selectedFile && (
                        <Button
                          variant="success"
                          size="sm"
                          className="mt-2"
                          onClick={handleImageUpload}
                          disabled={imageLoading}
                        >
                          {imageLoading ? 'Uploading...' : 'Save Image'}
                        </Button>
                      )}
                    </Form.Group>
                  </div>
                </Col>

                <Col md={8}>
                  <Form.Group className="mb-3">
                    <Form.Label>Full Name</Form.Label>
                    <Form.Control
                      type="text"
                      name="name"
                      value={customer.name}
                      onChange={handleInputChange}
                      required
                    />
                  </Form.Group>

                  <Form.Group className="mb-3">
                    <Form.Label>Email</Form.Label>
                    <Form.Control
                      type="email"
                      name="email"
                      value={customer.email}
                      onChange={handleInputChange}
                      required
                      disabled
                    />
                  </Form.Group>

                  <Form.Group className="mb-3">
                    <Form.Label>Contact</Form.Label>
                    <Form.Control
                      type="tel"
                      name="contact"
                      value={customer.contact}
                      onChange={handleInputChange}
                    />
                  </Form.Group>

                  <Form.Group className="mb-3">
                    <Form.Label>Address</Form.Label>
                    <Form.Control
                      as="textarea"
                      rows={3}
                      name="address"
                      value={customer.address}
                      onChange={handleInputChange}
                    />
                  </Form.Group>

                  <Form.Group className="mb-3">
                    <Form.Label>Gender</Form.Label>
                    <Form.Select
                      name="gender"
                      value={customer.gender}
                      onChange={handleInputChange}
                    >
                      <option value="MALE">Male</option>
                      <option value="FEMALE">Female</option>
                      <option value="OTHER">Other</option>
                    </Form.Select>
                  </Form.Group>

                  <div className="d-flex justify-content-end gap-2">
                    <Button variant="secondary" onClick={() => setEditMode(false)}>
                      Cancel
                    </Button>
                    <Button variant="primary" type="submit" disabled={loading}>
                      {loading ? 'Saving...' : <><FaSave /> Save Profile</>}
                    </Button>
                  </div>
                </Col>
              </Row>
            </Form>
          ) : (
            <Row>
              <Col md={4} className="text-center">
                <div className="profile-image-container">
                  {getProfileImageSrc() ? (
                    <Image
                      src={getProfileImageSrc()}
                      roundedCircle
                      className="profile-image"
                    />
                  ) : (
                    <div className="profile-image-placeholder">
                      <FaUser size={50} />
                    </div>
                  )}
                </div>
              </Col>

              <Col md={8}>
                <div className="profile-info">
                  <h4>{customer.name}</h4>
                  <p><strong>Email:</strong> {customer.email}</p>
                  <p><strong>Contact:</strong> {customer.contact || 'Not provided'}</p>
                  <p><strong>Address:</strong> {customer.address || 'Not provided'}</p>
                  <p><strong>Gender:</strong> {customer.gender}</p>
                </div>

                <div className="d-flex justify-content-end mt-4">
                  <Button variant="primary" onClick={() => setEditMode(true)}>
                    <FaEdit /> Edit Profile
                  </Button>
                </div>
              </Col>
            </Row>
          )}
        </Card.Body>
      </Card>
    </div>
  );
}

export default CProfile;
