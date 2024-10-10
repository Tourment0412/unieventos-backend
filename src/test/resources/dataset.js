db = connect("mongodb://localhost:27017/unieventosTest")

// Colección accounts
db.accounts.insertMany([
    {
        _id: ObjectId("6706047ac127c9d5e7e16cbf"),
        user: {
            phoneNumber: "1234567890",
            address: "Calle 1",
            dni: "1111111111",
            name: "Juan Perez"
        },
        email: "miraortega2020@gmail.com",
        password: "$2a$10$FdypU2Q7lbwzocQ2ljYpK.uoUSyU3Z8OLlOUWTEZIAK2ek4VpEpwq",
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
        _id: ObjectId("6706047ac127c9d5e7e16cc0"),
        user: {
            phoneNumber: "0987654321",
            address: "Calle 2",
            dni: "2222222222",
            name: "Maria Lopez"
        },
        email: "santiquinterouribe0412@gmail.com",
        password: "$2a$10$dWEGQefdBqXl.SsEuXuSoOBc79c1FMB8vfp6LIvcKKr3fWSCUycau",
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
        _id: ObjectId("6706047ac127c9d5e7e16cc1"),
        user: {
            phoneNumber: "1122334455",
            address: "Calle 3",
            dni: "3333333333",
            name: "Carlos Gomez"
        },
        email: "carlos.gomez@example.com",
        password: "$2a$10$dWEGQefdBqXl.SsEuXuSoOBc79c1FMB8vfp6LIvcKKr3fWSCUycau",
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
        _id: ObjectId("6706047ac127c9d5e7e16cc2"),
        user: {
            phoneNumber: "2233445566",
            address: "Calle 4",
            dni: "4444444444",
            name: "Ana Torres"
        },
        email: "ana.torres@example.com",
        password: "$2a$10$dWEGQefdBqXl.SsEuXuSoOBc79c1FMB8vfp6LIvcKKr3fWSCUycau",
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
        _id: ObjectId("67072d61d18f5db00879b7c6"),
        name: "New account",
        code: "NEW15P",
        type: "MULTIPLE",
        discount: 0.15,
        expirationDate: ISODate("2024-12-31T23:59:59Z"),
        status: "AVAILABLE"
    },
    {
        _id: ObjectId("67072d61d18f5db00879b7c7"),
        name: "Firts coupon",
        code: "FIRST1",
        type: "MULTIPLE",
        discount: 0.1,
        expirationDate: ISODate("2024-12-31T23:59:59Z"),
        status: "AVAILABLE"
    },
    {
        _id: ObjectId("6706047ac127c9d5e7e16cc3"),
        name: "Discount 10%",
        code: "DISC10",
        type: "UNIQUE",
        discount: 0.1,
        expirationDate: ISODate("2024-12-31T23:59:59Z"),
        status: "AVAILABLE"
    },
    {
        _id: ObjectId("6706047ac127c9d5e7e16cc4"),
        name: "Flat 5000",
        code: "FLAT5000",
        type: "MULTIPLE",
        discount: 0.75,
        expirationDate: ISODate("2023-12-31T23:59:59Z"),
        status: "NOT_AVAILABLE"
    },
    {
        _id: ObjectId("6706047ac127c9d5e7e16cc5"),
        name: "Black Friday",
        code: "BF2024",
        type: "MULTIPLE",
        discount: 0.5,
        expirationDate: ISODate("2024-11-30T23:59:59Z"),
        status: "AVAILABLE"
    }
]);


// Colección events
db.events.insertMany([
    {
        _id :  ObjectId("6706047ac127c9d5e7e16cc6"),
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
        _id :  ObjectId("6706047ac127c9d5e7e16cc7"),
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
        _id :  ObjectId("6706047ac127c9d5e7e16cc8"),
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
        _id :  ObjectId("6706047ac127c9d5e7e16cc9"),
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
            { name: "General", price: 50000, ticketsSold: 40, maxCapacity: 300 }
        ]
    },
    {
        _id :  ObjectId("6706047ac127c9d5e7e16cca"),
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
        _id :  ObjectId("6706047ac127c9d5e7e16ccb"),
        items: [
            { eventId: ObjectId("6706047ac127c9d5e7e16cc9"), price: 50000, locationName: "General", quantity: 2 },
            { eventId: ObjectId("6706047ac127c9d5e7e16cc7"), price: 150000, locationName: "VIP", quantity: 1 }
        ],
        gatewayCode: "MERCADOPAGO",
        date: new ISODate("2024-10-01T14:30:00Z"),
        total: 125000,
        clientId: ObjectId("6706047ac127c9d5e7e16cbf"),
        couponId: ObjectId("6706047ac127c9d5e7e16cc5"),
        payment: {
            id: "PAY-12345",
            currency: "COP",
            paymentType: "CREDIT_CARD",
            statusDetail: "Approved",
            authorizationCode: "AUTH-98765",
            date: new ISODate("2024-10-01T14:32:00Z"),
            transactionValue: 125000,
            status: "SUCCESS"
        },
        isGift: false,
        friendMail: null
    },
    {
        _id :  ObjectId("67060f8d0cae5fbda81e750c"),
        items: [
            { eventId: ObjectId("67060f8d0cae5fbda81e750d"), price: 60000, locationName: "Tribuna Norte", quantity: 4 }
        ],
        gatewayCode: "MERCADOPAGO",
        date: new ISODate("2024-09-20T10:15:00Z"),
        total: 240000,
        clientId: ObjectId("6706047ac127c9d5e7e16cbf"),
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
        friendMail: "santiquinterouribe0412@gmail.com"
    },
    {
        _id :  ObjectId("67060f8d0cae5fbda81e7510"),
        items: [
            { eventId: ObjectId("6706047ac127c9d5e7e16cc8"), price: 80000, locationName: "General", quantity: 3 }
        ],
        gatewayCode: "MERCADOPAGO",
        date: new ISODate("2024-11-05T18:45:00Z"),
        total: 120000,
        clientId: ObjectId("6706047ac127c9d5e7e16cbf"),
        couponId: ObjectId("6706047ac127c9d5e7e16cc5"),
        payment: {
            id: "PAY-23456",
            currency: "COP",
            paymentType: "PSE",
            statusDetail: "Pending",
            authorizationCode: "AUTH-11223",
            date: new ISODate("2024-11-05T18:50:00Z"),
            transactionValue: 120000,
            status: "PENDING"
        },
        isGift: false,
        friendMail: null
    },
    {
        _id :  ObjectId("67060f8d0cae5fbda81e7514"),
        items: [
            { eventId: ObjectId("6706047ac127c9d5e7e16cc8"), price: 100000, locationName: "Pasarela", quantity: 1 },
            { eventId: ObjectId("6706047ac127c9d5e7e16cc8"), price: 80000, locationName: "General", quantity: 2 }
        ],
        gatewayCode: "PAYPAL",
        date: new ISODate("2024-10-10T15:30:00Z"),
        total: 260000,
        clientId: ObjectId("6706047ac127c9d5e7e16cbf"),
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
        friendMail: "santiquinterouribe0412@gmail.com"
    }
]);


// Colección shoppingCars
db.shoppingCars.insertMany([
    {
        _id: ObjectId("6707006494e25e379df5bd95"),
        date: new ISODate("2024-12-20T10:15:00Z"),
        items: [
            { amount: 2, locationName: "General", idEvent: ObjectId("6706047ac127c9d5e7e16cc9") },
            { amount: 1, locationName: "VIP", idEvent: ObjectId("6706047ac127c9d5e7e16cc7") }
        ],
        userId: ObjectId("6706047ac127c9d5e7e16cbf")
    },
    {
        _id: ObjectId("67060f8d0cae5fbda81e751a"),
        date: new ISODate("2024-09-20T10:15:00Z"),
        items: [
            { amount: 4, locationName: "Tribuna Norte", idEvent: ObjectId("67060f8d0cae5fbda81e750d") }
        ],
        userId: ObjectId("6706047ac127c9d5e7e16cbf")
    },
    {
        _id: ObjectId("6706114a051852d809818d4c"),
        date: new ISODate("2024-11-05T18:45:00Z"),
        items: [
            { amount: 3, locationName: "General", idEvent: ObjectId("6706047ac127c9d5e7e16cc8") }
        ],
        userId: ObjectId("6706047ac127c9d5e7e16cbf")
    },
    {
        _id: ObjectId("6706114a051852d809818d4d"),
        date: new ISODate("2024-10-10T15:30:00Z"),
        items: [
            { amount: 1, locationName: "Pasarela", idEvent: ObjectId("6706047ac127c9d5e7e16cc8") },
            { amount: 2, locationName: "General", idEvent: ObjectId("6706047ac127c9d5e7e16cc8") }
        ],
        userId: ObjectId("6706047ac127c9d5e7e16cbf")
    }
]);
