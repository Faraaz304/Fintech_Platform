'use client'; // This is essential for using hooks

import React, { useState } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';

const RegisterPage = () => {
    const router = useRouter();
    // Use an object to hold all form data
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        phoneNumber: '',
        email: '',
        password: '',
        confirmPassword: ''
    });
    const [error, setError] = useState(null);
    const [isLoading, setIsLoading] = useState(false);

    
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        
        if (formData.password !== formData.confirmPassword) {
            setError("Passwords do not match.");
            return;
        }

        setIsLoading(true);
        setError(null);

        try {
            const response = await fetch('http://localhost:8090/api/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    firstName: formData.firstName, // Correct camelCase
                    lastName: formData.lastName,   // Correct camelCase
                    phoneNumber: formData.phoneNumber, 
                    email: formData.email,
                    password: formData.password
                }),
            });

            if (!response.ok) {
                const errorData = await response.json();
                const errorMessage = errorData.message || 'Failed to register. Please check your details.';
                throw new Error(errorMessage);
            }
            
            router.push('/Login?registered=true');

        } catch (error) {
            setError(error.message);
        } finally {
            setIsLoading(false);
        }
    };


    return (
        <div className="w-full max-w-md p-8 space-y-8 bg-white rounded-lg shadow-md">
            <div className="text-center">
                <h1 className="text-3xl font-bold text-gray-800">Create an Account</h1>
                <p className="mt-2 text-gray-600">Join us and start your financial journey today</p>
            </div>

            <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
                <div className="rounded-md shadow-sm space-y-2">
                    {/* Simplified input fields for brevity */}
                    <input name="firstName" type="text" required className="appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm" placeholder="First Name" value={formData.firstName} onChange={handleChange} />
                    <input name="lastName" type="text" required className="appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm" placeholder="Last Name" value={formData.lastName} onChange={handleChange} />
                    <input name="phoneNumber" type="tel" required className="appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm" placeholder="Phone Number" value={formData.phoneNumber} onChange={handleChange} />
                    <input name="email" type="email" required className="appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm" placeholder="Email address" value={formData.email} onChange={handleChange} />
                    <input name="password" type="password" required className="appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm" placeholder="Password" value={formData.password} onChange={handleChange} />
                    <input name="confirmPassword" type="password" required className="appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm" placeholder="Confirm Password" value={formData.confirmPassword} onChange={handleChange} />
                </div>
                
                {error && <p className="text-sm text-red-600 text-center">{error}</p>}

                <div>
                    <button type="submit" disabled={isLoading} className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:bg-blue-400">
                        {isLoading ? 'Creating Account...' : 'Create Account'}
                    </button>
                </div>
            </form>

            <div className="text-center text-sm text-gray-600">
                <p>
                    Already have an account?{' '}
                    <Link href="/Login" legacyBehavior>
                        <a className="font-medium text-blue-600 hover:text-blue-500">Sign in</a>
                    </Link>
                </p>
            </div>
        </div>
    );
};

export default RegisterPage;