class Fine {
  final String id;
  final String reference;
  final String status;
  final String category;
  final double amount;
  final String location;
  final String vehicleRegistration;
  final String issuedAt;

  Fine({
    required this.id,
    required this.reference,
    required this.status,
    required this.category,
    required this.amount,
    required this.location,
    required this.vehicleRegistration,
    required this.issuedAt,
  });

  factory Fine.fromJson(Map<String, dynamic> json) {
    return Fine(
      id: json['id'].toString(),
      reference: json['reference'],
      status: json['status'],
      category: json['categoryDescription'],
      amount: (json['amount'] as num).toDouble(),
      location: json['location'],
      vehicleRegistration: json['vehicleRegistration'],
      issuedAt: json['issuedAt'],
    );
  }
}
class PaymentResponse {
  final String transactionId;
  final String status;
  final double amount;

  PaymentResponse({
    required this.transactionId,
    required this.status,
    required this.amount,
  });

  factory PaymentResponse.fromJson(Map<String, dynamic> json) {
    return PaymentResponse(
      transactionId: json['transactionId'],
      status: json['status'] ?? 'SUCCESS',
      amount: (json['amount'] as num).toDouble(),
    );
  }
}

class User {
  final String id;
  final String email;
  final String fullName;
  final String token;

  User({
    required this.id,
    required this.email,
    required this.fullName,
    required this.token,
  });

  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      id: json['id'].toString(),
      email: json['email'],
      fullName: json['fullName'],
      token: json['token'],
    );
  }
}
