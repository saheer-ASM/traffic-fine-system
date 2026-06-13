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