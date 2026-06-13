import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/providers.dart';
import '../widgets/custom_widgets.dart';

class LoginScreen extends StatefulWidget {
  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final _emailController = TextEditingController();
  final _passwordController = TextEditingController();
  final _formKey = GlobalKey<FormState>();

  @override
  void dispose() {
    _emailController.dispose();
    _passwordController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: Consumer<AuthProvider>(
          builder: (context, authProvider, _) => SingleChildScrollView(
            padding: const EdgeInsets.all(24),
            child: Form(
              key: _formKey,
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const SizedBox(height: 60),
                  Text('Traffic Fine System', style: Theme.of(context).textTheme.headlineMedium),
                  const SizedBox(height: 8),
                  Text('Enter your credentials',
                      style: Theme.of(context).textTheme.bodyMedium),
                  const SizedBox(height: 48),
                  AppTextField(
                    label: 'Email',
                    controller: _emailController,
                    validator: (value) {
                      if (value?.isEmpty ?? true) return 'Email is required';
                      return null;
                    },
                  ),
                  const SizedBox(height: 16),
                  AppTextField(
                    label: 'Password',
                    controller: _passwordController,
                    isPassword: true,
                    validator: (value) {
                      if (value?.isEmpty ?? true) return 'Password is required';
                      return null;
                    },
                  ),
                  const SizedBox(height: 8),
                  if (authProvider.error != null)
                    Padding(
                      padding: const EdgeInsets.only(top: 8),
                      child: Text(authProvider.error!,
                          style: const TextStyle(color: Colors.red)),
                    ),
                  const SizedBox(height: 32),
                  AppButton(
                    label: 'Login',
                    isLoading: authProvider.isLoading,
                    onPressed: () async {
                      if (_formKey.currentState!.validate()) {
                        await authProvider.login(
                          _emailController.text,
                          _passwordController.text,
                        );
                        if (authProvider.isAuthenticated) {
                          Navigator.of(context).pushReplacementNamed('/home');
                        }
                      }
                    },
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}

class HomeScreen extends StatefulWidget {
  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final _referenceController = TextEditingController();

  @override
  void dispose() {
    _referenceController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Fine Payment Portal'),
        actions: [
          IconButton(
            icon: const Icon(Icons.logout),
            onPressed: () {
              Provider.of<AuthProvider>(context, listen: false).logout();
              Navigator.of(context).pushReplacementNamed('/login');
            },
          ),
        ],
      ),
      body: Consumer2<FineProvider, PaymentProvider>(
        builder: (context, fineProvider, paymentProvider, _) => SingleChildScrollView(
          padding: const EdgeInsets.all(16),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const SizedBox(height: 16),
              Text('Search Fine', style: Theme.of(context).textTheme.titleLarge),
              const SizedBox(height: 16),
              AppTextField(
                label: 'Fine Reference Number',
                controller: _referenceController,
              ),
              const SizedBox(height: 16),
              AppButton(
                label: 'Search',
                isLoading: fineProvider.isLoading,
                onPressed: () {
                  if (_referenceController.text.isNotEmpty) {
                    fineProvider.searchFine(_referenceController.text);
                  }
                },
              ),
              if (fineProvider.error != null)
                Padding(
                  padding: const EdgeInsets.only(top: 16),
                  child: Text(fineProvider.error!, style: const TextStyle(color: Colors.red)),
                ),
              if (fineProvider.fine != null) ...[
                const SizedBox(height: 24),
                FineCard(
                  reference: fineProvider.fine!.reference,
                  category: fineProvider.fine!.category,
                  amount: fineProvider.fine!.amount,
                  status: fineProvider.fine!.status,
                ),
                if (fineProvider.fine!.status == 'PENDING') ...[
                  const SizedBox(height: 16),
                  AppButton(
                    label: 'Pay Fine',
                    isLoading: paymentProvider.isProcessing,
                    onPressed: () {
                      _showPaymentDialog(context, fineProvider, paymentProvider);
                    },
                  ),
                ],
              ],
            ],
          ),
        ),
      ),
    );
  }

  void _showPaymentDialog(BuildContext context, FineProvider fineProvider,
      PaymentProvider paymentProvider) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Process Payment'),
        content: const Text('Confirm payment via mobile payment gateway?'),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('Cancel'),
          ),
          ElevatedButton(
            onPressed: () async {
              final fine = fineProvider.fine!;
              final success = await paymentProvider.processPayment(
                int.parse(fine.id),
                'MOBILE_PAYMENT',
                'gateway_ref_${DateTime.now().millisecondsSinceEpoch}',
              );
              Navigator.pop(context);
              if (success) {
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(content: Text('Payment successful!')),
                );
                fineProvider.clearFine();
                _referenceController.clear();
              }
            },
            child: const Text('Pay'),
          ),
        ],
      ),
    );
  }
}
