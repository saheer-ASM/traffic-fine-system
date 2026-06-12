import { useState, useEffect } from 'react';
import { BarChart, Bar, LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, PieChart, Pie, Cell } from 'recharts';
import { adminService } from '../services/api';
import { StatCard, Loading, ErrorMessage } from '../components/Common';

const COLORS = ['#3b82f6', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6'];

export const DashboardPage = () => {
  const [stats, setStats] = useState(null);
  const [districts, setDistricts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      const [statsRes, districtsRes, categoriesRes] = await Promise.all([
        adminService.getDashboardStats(),
        adminService.getDistrictCollections(),
        adminService.getCategoryCollections(),
      ]);

      if (statsRes.data.success) setStats(statsRes.data.data);
      if (districtsRes.data.success) setDistricts(districtsRes.data.data);
      if (categoriesRes.data.success) setCategories(categoriesRes.data.data);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to load dashboard data');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <Loading />;

  const collectionRate = stats?.totalFines > 0 
    ? ((stats?.totalPaidFines / stats?.totalFines) * 100).toFixed(1)
    : 0;

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      <h1 className="text-4xl font-bold text-gray-800 mb-8">Dashboard</h1>

      {error && <div className="mb-4"><ErrorMessage message={error} /></div>}

      {/* Key Metrics */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-4 mb-8">
        <StatCard
          title="Total Fines"
          value={stats?.totalFines || 0}
          color="blue"
        />
        <StatCard
          title="Paid Fines"
          value={stats?.totalPaidFines || 0}
          color="green"
        />
        <StatCard
          title="Pending Fines"
          value={stats?.totalPendingFines || 0}
          color="red"
        />
        <StatCard
          title="Collections Rate"
          value={`${collectionRate}%`}
          color="purple"
        />
        <StatCard
          title="Total Collected"
          value={`Rs. ${(stats?.totalCollections || 0).toFixed(0)}`}
          subtitle="All time"
          color="blue"
        />
      </div>

      {/* Charts */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 mb-8">
        {/* District Collections */}
        <div className="bg-white rounded-lg shadow-md p-6">
          <h2 className="text-xl font-semibold text-gray-800 mb-4">Collections by District</h2>
          {districts.length > 0 ? (
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={districts}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="district" angle={-45} textAnchor="end" height={80} />
                <YAxis />
                <Tooltip />
                <Legend />
                <Bar dataKey="collectedAmount" fill="#3b82f6" name="Collected" />
                <Bar dataKey="totalAmount" fill="#d1d5db" name="Total Amount" />
              </BarChart>
            </ResponsiveContainer>
          ) : (
            <p className="text-gray-500">No data available</p>
          )}
        </div>

        {/* Category Distribution */}
        <div className="bg-white rounded-lg shadow-md p-6">
          <h2 className="text-xl font-semibold text-gray-800 mb-4">Fines by Category</h2>
          {categories.length > 0 ? (
            <ResponsiveContainer width="100%" height={300}>
              <PieChart>
                <Pie
                  data={categories}
                  dataKey="totalFines"
                  nameKey="categoryCode"
                  cx="50%"
                  cy="50%"
                  outerRadius={80}
                  label
                >
                  {categories.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip />
              </PieChart>
            </ResponsiveContainer>
          ) : (
            <p className="text-gray-500">No data available</p>
          )}
        </div>
      </div>

      {/* Districts Table */}
      <div className="bg-white rounded-lg shadow-md p-6 mb-8">
        <h2 className="text-xl font-semibold text-gray-800 mb-4">District Details</h2>
        <div className="overflow-x-auto">
          <table className="w-full text-sm">
            <thead>
              <tr className="border-b">
                <th className="text-left py-2 px-4">District</th>
                <th className="text-right py-2 px-4">Total Fines</th>
                <th className="text-right py-2 px-4">Paid Fines</th>
                <th className="text-right py-2 px-4">Collection Rate</th>
                <th className="text-right py-2 px-4">Amount Collected</th>
              </tr>
            </thead>
            <tbody>
              {districts.map((district) => {
                const rate = district.totalFines > 0 
                  ? ((district.paidFines / district.totalFines) * 100).toFixed(1)
                  : 0;
                return (
                  <tr key={district.district} className="border-b hover:bg-gray-50">
                    <td className="py-2 px-4">{district.district}</td>
                    <td className="text-right py-2 px-4">{district.totalFines}</td>
                    <td className="text-right py-2 px-4">{district.paidFines}</td>
                    <td className="text-right py-2 px-4">{rate}%</td>
                    <td className="text-right py-2 px-4">Rs. {district.collectedAmount.toFixed(0)}</td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
