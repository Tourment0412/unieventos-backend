db = connect("mongodb://localhost:27017/unieventosTest")

// Colección accounts
db.accounts.insertMany([
    {
        _id: ObjectId("6529c8e84f7f4e001c7ebf85"),
        user: { /* Aquí define los atributos del usuario, por ejemplo: */
            phoneNumber: "1234567890",
            address: "Calle 1",
            dni: "1111111111",
            name: "Juan Perez"
        },
        email: "juan.perez@example.com",
        password: "password123",
        registrationValidationCode: {
            creationDate: new ISODate("2024-10-01T10:00:00Z"),
            code: "VALIDATION123"
        },
        role: "CLIENT",
        registrationDate: new ISODate("2024-10-01T10:00:00Z"),
        status: "ACTIVE",
        passwordValidationCode: {
            creationDate: new ISODate("2024-10-01T10:00:00Z"),
            code: "PASSWORDVALIDATION123"
        }
    },
    {
        _id: ObjectId("6529c8e84f7f4e001c7ebf95"),
        user: {
            phoneNumber: "0987654321",
            address: "Calle 2",
            dni: "2222222222",
            name: "Maria Lopez"
        },
        email: "maria.lopez@example.com",
        password: "password456",
        registrationValidationCode: {
            creationDate: new ISODate("2024-10-01T10:00:00Z"),
            code: "VALIDATION456"
        },
        role: "ADMIN",
        registrationDate: new ISODate("2024-10-01T10:00:00Z"),
        status: "ACTIVE",
        passwordValidationCode: {
            creationDate: new ISODate("2024-10-01T10:00:00Z"),
            code: "PASSWORDVALIDATION456"
        }
    },
    {
        user: {
            phoneNumber: "1122334455",
            address: "Calle 3",
            dni: "3333333333",
            name: "Carlos Gomez"
        },
        email: "carlos.gomez@example.com",
        password: "password789",
        registrationValidationCode: {
            creationDate: new ISODate("2024-10-01T10:00:00Z"),
            code: "VALIDATION789"
        },
        role: "CLIENT",
        registrationDate: new ISODate("2024-10-01T10:00:00Z"),
        status: "INACTIVE",
        passwordValidationCode: {
            creationDate: new ISODate("2024-10-01T10:00:00Z"),
            code: "PASSWORDVALIDATION789"
        }
    },
    {
        user: {
            phoneNumber: "2233445566",
            address: "Calle 4",
            dni: "4444444444",
            name: "Ana Torres"
        },
        email: "ana.torres@example.com",
        password: "password101112",
        registrationValidationCode: {
            creationDate: new ISODate("2024-10-01T10:00:00Z"),
            code: "VALIDATION101112"
        },
        role: "ADMIN",
        registrationDate: new ISODate("2024-10-01T10:00:00Z"),
        status: "DELETED",
        passwordValidationCode: {
            creationDate: new ISODate("2024-10-01T10:00:00Z"),
            code: "PASSWORDVALIDATION101112"
        }
    }
]);




// Colección coupons
db.coupons.insertMany([
    {
        name: "Descuento del 10%",
        type: "UNIQUE",
        status: "AVAILABLE",
        code: "DESCUENTO10",
        expirationDate: new ISODate("2024-12-31T23:59:59Z"),
        discount: 0.1
    },
    {
        name: "Descuento del 20%",
        type: "UNIQUE",
        status: "AVAILABLE",
        code: "DESCUENTO20",
        expirationDate: new ISODate("2024-11-30T23:59:59Z"),
        discount: 0.2
    },
    {
        name: "Promoción de Fin de Año",
        type: "MULTIPLE",
        status: "AVAILABLE",
        code: "FINDEANO",
        expirationDate: new ISODate("2024-12-31T23:59:59Z"),
        discount: 0.15
    },
    {
        name: "Cupon de Bienvenida",
        type: "UNIQUE",
        status: "NOT_AVAILABLE",
        code: "BIENVENIDA",
        expirationDate: new ISODate("2024-10-31T23:59:59Z"),
        discount: 0.25
    },
    {
        name: "Descuento por Referido",
        type: "MULTIPLE",
        status: "AVAILABLE",
        code: "REFERIDO",
        expirationDate: new ISODate("2025-01-31T23:59:59Z"),
        discount: 0.05
    }
]);


// Colección events
db.events.insertMany([
    {
        _id :  ObjectId("6529c8e84f7f4e001c7ebf92"),
        name: "Concierto de Rock",
        address: "Calle 10 #25-35",
        city: "Bogotá",
        coverImage: "cover_rock_concert.jpg",
        localitiesImage: "localities_rock_concert.jpg",
        date: new ISODate("2024-11-15T20:00:00Z"),
        description: "Concierto de rock con las mejores bandas locales e internacionales.",
        type: "CONCERT",
        status: "ACTIVE",
        locations: [
            { name: "General", price: 100000, ticketsSold: 50, maxCapacity: 200 },
            { name: "VIP", price: 200000, ticketsSold: 30, maxCapacity: 100 }
        ]
    },
    {
        _id: ObjectId("6529c8e84f7f4e001c7ebf93"),
        name: "Partido de Fútbol",
        address: "Estadio Metropolitano",
        city: "Barranquilla",
        coverImage: "cover_football_match.jpg",
        localitiesImage: "localities_football_match.jpg",
        date: new ISODate("2024-12-10T18:00:00Z"),
        description: "Partido entre los mejores equipos de la liga nacional.",
        type: "SPORT",
        status: "ACTIVE",
        locations: [
            { name: "Tribuna Norte", price: 50000, ticketsSold: 300, maxCapacity: 1000 },
            { name: "Tribuna Sur", price: 50000, ticketsSold: 200, maxCapacity: 1000 },
            { name: "VIP", price: 150000, ticketsSold: 50, maxCapacity: 200 }
        ]
    },
    {
        name: "Desfile de Moda",
        address: "Centro de Convenciones",
        city: "Medellín",
        coverImage: "cover_fashion_show.jpg",
        localitiesImage: "localities_fashion_show.jpg",
        date: new ISODate("2025-01-20T19:00:00Z"),
        description: "Presentación de las últimas tendencias de moda por diseñadores reconocidos.",
        type: "FASHION",
        status: "ACTIVE",
        locations: [
            { name: "Pasarela", price: 250000, ticketsSold: 20, maxCapacity: 100 },
            { name: "General", price: 80000, ticketsSold: 40, maxCapacity: 200 }
        ]
    },
    {
        name: "Obra de Teatro Cultural",
        address: "Teatro Nacional",
        city: "Cali",
        coverImage: "cover_theater.jpg",
        localitiesImage: "localities_theater.jpg",
        date: new ISODate("2024-10-25T19:30:00Z"),
        description: "Obra de teatro cultural sobre la historia del país.",
        type: "CULTURAL",
        status: "INACTIVE",
        locations: [
            { name: "Palco", price: 180000, ticketsSold: 10, maxCapacity: 50 },
            { name: "General", price: 60000, ticketsSold: 40, maxCapacity: 300 }
        ]
    },
    {
        name: "Evento de Belleza",
        address: "Salón de Exposiciones",
        city: "Cartagena",
        coverImage: "cover_beauty_event.jpg",
        localitiesImage: "localities_beauty_event.jpg",
        date: new ISODate("2024-11-05T10:00:00Z"),
        description: "Evento de belleza con los mejores productos y servicios del sector.",
        type: "BEAUTY",
        status: "ACTIVE",
        locations: [
            { name: "Sala Principal", price: 120000, ticketsSold: 80, maxCapacity: 150 },
            { name: "Zona VIP", price: 250000, ticketsSold: 20, maxCapacity: 50 }
        ]
    }
]);


// Colección orders
db.orders.insertMany([
    {
        items: [
            { eventId: ObjectId("6529c8e84f7f4e001c7ebf92"), price: 50000, locationName: "General", quantity: 2 },
            { eventId: ObjectId("6529c8e84f7f4e001c7ebf93"), price: 150000, locationName: "VIP", quantity: 1 }
        ],
        gatewayCode: "MERCADOPAGO",
        date: new ISODate("2024-10-01T14:30:00Z"),
        total: 250000,
        clientId: ObjectId("6529c8e84f7f4e001c7ebf85"),
        couponId: ObjectId("6529c8e84f7f4e001c7ebf95"),
        payment: {
            id: "PAY-12345",
            currency: "COP",
            paymentType: "CREDIT_CARD",
            statusDetail: "Approved",
            authorizationCode: "AUTH-98765",
            date: new ISODate("2024-10-01T14:32:00Z"),
            transactionValue: 250000,
            status: "SUCCESS"
        },
        isGift: false,
        friendMail: null
    },
    {
        items: [
            { eventId: ObjectId("6529c8e84f7f4e001c7ebf94"), price: 60000, locationName: "Tribuna Norte", quantity: 4 }
        ],
        gatewayCode: "MERCADOPAGO",
        date: new ISODate("2024-09-20T10:15:00Z"),
        total: 240000,
        clientId: ObjectId("6529c8e84f7f4e001c7ebf86"),
        couponId: null,
        payment: {
            id: "PAY-67890",
            currency: "COP",
            paymentType: "DEBIT_CARD",
            statusDetail: "Approved",
            authorizationCode: "AUTH-54321",
            date: new ISODate("2024-09-20T10:20:00Z"),
            transactionValue: 240000,
            status: "SUCCESS"
        },
        isGift: true,
        friendMail: "friend@example.com"
    },
    {
        items: [
            { eventId: ObjectId("6529c8e84f7f4e001c7ebf95"), price: 80000, locationName: "General", quantity: 3 }
        ],
        gatewayCode: "MERCADOPAGO",
        date: new ISODate("2024-11-05T18:45:00Z"),
        total: 240000,
        clientId: ObjectId("6529c8e84f7f4e001c7ebf87"),
        couponId: ObjectId("6529c8e84f7f4e001c7ebf96"),
        payment: {
            id: "PAY-23456",
            currency: "COP",
            paymentType: "PSE",
            statusDetail: "Pending",
            authorizationCode: "AUTH-11223",
            date: new ISODate("2024-11-05T18:50:00Z"),
            transactionValue: 240000,
            status: "PENDING"
        },
        isGift: false,
        friendMail: null
    },
    {
        items: [
            { eventId: ObjectId("6529c8e84f7f4e001c7ebf96"), price: 100000, locationName: "Pasarela", quantity: 1 },
            { eventId: ObjectId("6529c8e84f7f4e001c7ebf97"), price: 80000, locationName: "General", quantity: 2 }
        ],
        gatewayCode: "PAYPAL",
        date: new ISODate("2024-10-10T15:30:00Z"),
        total: 260000,
        clientId: ObjectId("6529c8e84f7f4e001c7ebf88"),
        couponId: null,
        payment: {
            id: "PAY-98765",
            currency: "USD",
            paymentType: "PAYPAL",
            statusDetail: "Approved",
            authorizationCode: "AUTH-33445",
            date: new ISODate("2024-10-10T15:35:00Z"),
            transactionValue: 260000,
            status: "SUCCESS"
        },
        isGift: true,
        friendMail: "bestfriend@example.com"
    }
]);


// Colección shoppingCars
db.shoppingCars.insertMany([
    {
        date: new ISODate("2024-10-01T14:30:00Z"),
        items: [
            { amount: 2, locationName: "General", idEvent: ObjectId("6529d8e94f7f4e001c7ebf92") },
            { amount: 1, locationName: "VIP", idEvent: ObjectId("6529d8e94f7f4e001c7ebf93") }
        ],
        userId: ObjectId("6529d8e94f7f4e001c7ebf85")
    },
    {
        date: new ISODate("2024-09-20T10:15:00Z"),
        items: [
            { amount: 4, locationName: "Tribuna Norte", idEvent: ObjectId("6529d8e94f7f4e001c7ebf94") }
        ],
        userId: ObjectId("6529d8e94f7f4e001c7ebf86")
    },
    {
        date: new ISODate("2024-11-05T18:45:00Z"),
        items: [
            { amount: 3, locationName: "General", idEvent: ObjectId("6529d8e94f7f4e001c7ebf95") }
        ],
        userId: ObjectId("6529d8e94f7f4e001c7ebf87")
    },
    {
        date: new ISODate("2024-10-10T15:30:00Z"),
        items: [
            { amount: 1, locationName: "Pasarela", idEvent: ObjectId("6529d8e94f7f4e001c7ebf96") },
            { amount: 2, locationName: "General", idEvent: ObjectId("6529d8e94f7f4e001c7ebf97") }
        ],
        userId: ObjectId("6529d8e94f7f4e001c7ebf88")
    }
]);
