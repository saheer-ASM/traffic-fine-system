import 'package:flutter/material.dart';
import '../models/models.dart';
import '../services/api_service.dart';

class AuthProvider extends ChangeNotifier {
  User? _user;
  bool _isLoading = false;
  String? _error;

  User? get user => _user;
  bool get isLoading => _isLoading;
  String? get error => _error;
  bool get isAuthenticated => _user != null;

  Future<void> login(String email, String password) async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      _user = await ApiService.login(email, password);
      _error = null;
    } catch (e) {
      _error = e.toString();
      _user = null;
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> logout() async {
    await ApiService.logout();
    _user = null;
    notifyListeners();
  }
}

class FineProvider extends ChangeNotifier {
  Fine? _fine;
  bool _isLoading = false;
  String? _error;

  Fine? get fine => _fine;
  bool get isLoading => _isLoading;
  String? get error => _error;

  Future<void> searchFine(String reference) async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      _fine = await ApiService.getFineByReference(reference);
      _error = null;
    } catch (e) {
      _error = e.toString();
      _fine = null;
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  void clearFine() {
    _fine = null;
    _error = null;
    notifyListeners();
  }
}

class PaymentProvider extends ChangeNotifier {
  bool _isProcessing = false;
  String? _error;
  PaymentResponse? _lastPayment;

  bool get isProcessing => _isProcessing;
  String? get error => _error;
  PaymentResponse? get lastPayment => _lastPayment;

  Future<bool> processPayment(
    int fineId,
    String paymentMethod,
    String paymentGatewayRef,
  ) async {
    _isProcessing = true;
    _error = null;
    notifyListeners();

    try {
      _lastPayment = await ApiService.processPayment(
        fineId,
        paymentMethod,
        paymentGatewayRef,
      );
      _error = null;
      return true;
    } catch (e) {
      _error = e.toString();
      _lastPayment = null;
      return false;
    } finally {
      _isProcessing = false;
      notifyListeners();
    }
  }
}
