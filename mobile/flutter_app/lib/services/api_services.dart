import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';
import '../models/models.dart';

class ApiService {
  static const String baseUrl = 'http://localhost:8080/api';
  static late SharedPreferences _prefs;
  static String? _token;

  static Future<void> init() async {
    _prefs = await SharedPreferences.getInstance();
    _token = _prefs.getString('authToken');
  }

  static Future<User> login(String email, String password) async {
    final response = await http.post(
      Uri.parse('$baseUrl/auth/login'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({'email': email, 'password': password}),
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      if (data['success']) {
        _token = data['data']['token'];
        await _prefs.setString('authToken', _token!);
        return User.fromJson({...data['data']['user'], 'token': _token});
      }
    }
    throw Exception('Login failed');
  }

  static Future<Fine> getFineByReference(String reference) async {
    final response = await http.get(
      Uri.parse('$baseUrl/fines/reference/$reference'),
      headers: _getHeaders(),
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      if (data['success']) {
        return Fine.fromJson(data['data']);
      }
    }
    throw Exception('Failed to fetch fine');
  }

  static Future<PaymentResponse> processPayment(
    int fineId,
    String paymentMethod,
    String paymentGatewayRef,
  ) async {
    final response = await http.post(
      Uri.parse('$baseUrl/payments'),
      headers: _getHeaders(),
      body: jsonEncode({
        'fineId': fineId,
        'paymentMethod': paymentMethod,
        'paymentGatewayReference': paymentGatewayRef,
      }),
    );

    if (response.statusCode == 201) {
      final data = jsonDecode(response.body);
      if (data['success']) {
        return PaymentResponse.fromJson(data['data']);
      }
    }
    throw Exception('Payment processing failed');
  }

  static Map<String, String> _getHeaders() {
    return {
      'Content-Type': 'application/json',
      if (_token != null) 'Authorization': 'Bearer $_token',
    };
  }

  static Future<void> logout() async {
    _token = null;
    await _prefs.remove('authToken');
  }

  static bool get isAuthenticated => _token != null;
}
