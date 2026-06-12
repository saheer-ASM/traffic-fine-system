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
