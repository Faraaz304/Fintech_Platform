import React from 'react';
import Link from 'next/link';
// Make sure to install react-icons: npm install react-icons
import { FaUserFriends, FaPiggyBank, FaBriefcase } from 'react-icons/fa';

const services = [
  {
    icon: <FaUserFriends size={48} className="text-blue-600 mb-4" />,
    title: 'Personal Banking',
    description: 'Checking, savings, and credit card solutions tailored to your individual financial needs.',
    link: '/personal',
  },
  {
    icon: <FaPiggyBank size={48} className="text-blue-600 mb-4" />,
    title: 'Loans & Mortgages',
    description: 'Competitive rates for home, auto, and personal loans to help you achieve your dreams.',
    link: '/loans',
  },
  {
    icon: <FaBriefcase size={48} className="text-blue-600 mb-4" />,
    title: 'Business Banking',
    description: 'Powerful financial tools, from checking accounts to commercial loans, to grow your business.',
    link: '/business',
  },
];

const ServicesSection = () => {
  return (
    <section className="bg-gray-50 py-16 md:py-24">
      <div className="container mx-auto px-4">
        {/* Section Header */}
        <div className="text-center mb-12">
          <h2 className="text-3xl md:text-4xl font-bold text-gray-800">
            Banking Designed for You
          </h2>
          <p className="text-lg text-gray-600 mt-2">
            Whatever your financial goals, we have the right solution.
          </p>
        </div>
        
        {/* Service Cards */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          {services.map((service, index) => (
            <div 
              key={index} 
              className="bg-white p-8 rounded-lg shadow-md hover:shadow-xl hover:-translate-y-2 transition-all duration-300 flex flex-col items-center text-center"
            >
              {service.icon}
              <h3 className="text-2xl font-semibold text-gray-800 mb-2">{service.title}</h3>
              <p className="text-gray-600 mb-6 flex-grow">{service.description}</p>
              <Link 
                href={service.link}
                className="text-blue-600 font-semibold hover:text-blue-800 transition-colors"
              >
                Learn More &rarr;
              </Link>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};

export default ServicesSection;