import { useState, useEffect } from 'react';
import { fineService } from '../services/api';
import { Loading, ErrorMessage, SuccessMessage } from '../components/Common';

export const DashboardPage = () => {
  const [reference, setReference] = useState('');
  const [fine, setFine] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleSearchFine = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setFine(null);
    setLoading(true);

    try {
      const { data } = await fineService.getFineByReference(reference);
      if (data.success) {
        setFine(data.data);
        setSuccess('Fine found!');
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Fine not found');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-6xl mx-auto px-4 py-8">
      <h1 className="text-4xl font-bold text-gray-800 mb-8">My Fines Dashboard</h1>

      {/* Search Section */}
      <div className="bg-white rounded-lg shadow-md p-6 mb-8">
        <h2 className="text-2xl font-semibold text-gray-800 mb-4">Search Fine</h2>
        
        <form onSubmit={handleSearchFine} className="space-y-4">
          <div className="flex gap-2">
            <input
              type="text"
              value={reference}
              onChange={(e) => setReference(e.target.value)}
              placeholder="Enter fine reference number (e.g., FIN1699500000123ABC)"
              className="flex-1 px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <button
              type="submit"
              disabled={loading}
              className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg transition disabled:opacity-50"
            >
              {loading ? 'Searching...' : 'Search'}
            </button>
          </div>
        </form>

        {error && <div className="mt-4"><ErrorMessage message={error} /></div>}
        {success && <div className="mt-4"><SuccessMessage message={success} /></div>}
      </div>

      {/* Fine Details */}
      {fine && (
        <div className="bg-white rounded-lg shadow-md p-6">
          <h2 className="text-2xl font-semibold text-gray-800 mb-4">Fine Details</h2>
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
            <div>
              <p className="text-gray-600 text-sm">Reference Number</p>
              <p className="text-lg font-semibold text-gray-800">{fine.reference}</p>
            </div>
            <div>
              <p className="text-gray-600 text-sm">Status</p>
              <span className={`text-lg font-semibold ${fine.status === 'PAID' ? 'text-green-600' : 'text-red-600'}`}>
                {fine.status}
              </span>
            </div>
            <div>
              <p className="text-gray-600 text-sm">Category</p>
              <p className="text-lg font-semibold text-gray-800">{fine.categoryDescription}</p>
            </div>
            <div>
              <p className="text-gray-600 text-sm">Amount</p>
              <p className="text-lg font-semibold text-gray-800">Rs. {fine.amount.toFixed(2)}</p>
            </div>
            <div>
              <p className="text-gray-600 text-sm">Location</p>
              <p className="text-lg font-semibold text-gray-800">{fine.location}</p>
            </div>
            <div>
              <p className="text-gray-600 text-sm">Issued At</p>
              <p className="text-lg font-semibold text-gray-800">{new Date(fine.issuedAt).toLocaleDateString()}</p>
            </div>
          </div>

          {fine.status === 'PENDING' && (
            <button className="bg-green-600 hover:bg-green-700 text-white px-6 py-2 rounded-lg transition">
              Pay Fine
            </button>
          )}
        </div>
      )}
    </div>
  );
};
