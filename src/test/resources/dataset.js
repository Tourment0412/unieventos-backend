db = connect("mongodb://localhost:27017/unieventosTest")

// Colección accounts
db.cuentas.insertMany([
    {
        _id: ObjectId("64f6d15801b1fc6c7c2037d1"),
        name: "John Doe",
        email: "johndoe@gmail.com",
        password: "hashed_password1",
        role: "client",
        status: "active"
    },
    {
        _id: ObjectId("64f6d15801b1fc6c7c2037d2"),
        name: "Jane Smith",
        email: "janesmith@gmail.com",
        password: "hashed_password2",
        role: "admin",
        status: "active"
    },
    {
        _id: ObjectId("64f6d15801b1fc6c7c2037d3"),
        name: "Carlos Perez",
        email: "carlosperez@gmail.com",
        password: "hashed_password3",
        role: "client",
        status: "inactive"
    }
]);

// Colección coupons
db.coupons.insertMany([
    {
        _id: ObjectId("64f6d15801b1fc6c7c2037d4"),
        name: "Discount 10%",
        code: "DISC10",
        CouponType: "percentage",
        discount: 0.1,
        expirationDate: ISODate("2024-12-31T23:59:59Z"),
        status: "active"
    },
    {
        _id: ObjectId("64f6d15801b1fc6c7c2037d5"),
        name: "Flat 5000",
        code: "FLAT5000",
        CouponType: "flat",
        discount: 0.5,
        expirationDate: ISODate("2023-12-31T23:59:59Z"),
        status: "expired"
    },
    {
        _id: ObjectId("64f6d15801b1fc6c7c2037d6"),
        name: "Black Friday",
        code: "BF2024",
        CouponType: "percentage",
        discount: 0.75,
        expirationDate: ISODate("2024-11-30T23:59:59Z"),
        status: "active"
    }
]);

// Colección events
db.events.insertMany([
    {
        _id: ObjectId("64f6d15801b1fc6c7c2037d7"),
        title: "Concert 2024",
        location: "Stadium A",
        date: ISODate("2024-06-01T19:00:00Z"),
        capacity: 10000,
        availableTickets: 5000,
        price: 100000
    },
    {
        _id: ObjectId("64f6d15801b1fc6c7c2037d8"),
        title: "Tech Conference",
        location: "Convention Center",
        date: ISODate("2024-09-15T09:00:00Z"),
        capacity: 2000,
        availableTickets: 200,
        price: 50000
    },
    {
        _id: ObjectId("64f6d15801b1fc6c7c2037d9"),
        title: "Art Expo 2024",
        location: "Gallery Z",
        date: ISODate("2024-08-20T10:00:00Z"),
        capacity: 1000,
        availableTickets: 800,
        price: 30000
    }
]);

// Colección orders
db.orders.insertMany([
    {
        _id: ObjectId("64f6d15801b1fc6c7c2037da"),
        clientId: ObjectId("64f6d15801b1fc6c7c2037d1"),
        date: ISODate("2024-01-01T12:00:00Z"),
        items: [
            { eventId: ObjectId("64f6d15801b1fc6c7c2037d7"), quantity: 2, price: 100000 },
            { eventId: ObjectId("64f6d15801b1fc6c7c2037d8"), quantity: 1, price: 50000 }
        ],
        total: 250000,
        paymentType: "credit_card",
        status: "paid",
        paymentDate: ISODate("2024-01-01T12:30:00Z")
    },
    {
        _id: ObjectId("64f6d15801b1fc6c7c2037db"),
        clientId: ObjectId("64f6d15801b1fc6c7c2037d2"),
        date: ISODate("2024-01-15T16:00:00Z"),
        items: [
            { eventId: ObjectId("64f6d15801b1fc6c7c2037d9"), quantity: 4, price: 30000 }
        ],
        total: 120000,
        paymentType: "paypal",
        status: "pending",
        paymentDate: null
    },
    {
        _id: ObjectId("64f6d15801b1fc6c7c2037dc"),
        clientId: ObjectId("64f6d15801b1fc6c7c2037d3"),
        date: ISODate("2024-02-01T10:00:00Z"),
        items: [
            { eventId: ObjectId("64f6d15801b1fc6c7c2037d8"), quantity: 3, price: 50000 }
        ],
        total: 150000,
        paymentType: "cash",
        status: "paid",
        paymentDate: ISODate("2024-02-01T10:30:00Z")
    }
]);

// Colección shoppingCars
db.shoppingCars.insertMany([
    {
        _id: ObjectId("67019b1d63e5b8567aabf871"),
        clientId: ObjectId("64f6d15801b1fc6c7c2037d1"),
        items: [
            { eventId: ObjectId("64f6d15801b1fc6c7c2037d7"), quantity: 1 },
            { eventId: ObjectId("64f6d15801b1fc6c7c2037d8"), quantity: 2 }
        ]
    },
    {
        _id: ObjectId("64f6d15801b1fc6c7c2037de"),
        clientId: ObjectId("64f6d15801b1fc6c7c2037d2"),
        items: [
            { eventId: ObjectId("64f6d15801b1fc6c7c2037d9"), quantity: 1 }
        ]
    },
    {
        _id: ObjectId("64f6d15801b1fc6c7c2037df"),
        clientId: ObjectId("64f6d15801b1fc6c7c2037d3"),
        items: [
            { eventId: ObjectId("64f6d15801b1fc6c7c2037d8"), quantity: 3 }
        ]
    }
]);
