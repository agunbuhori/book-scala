# Book Order API (Play Framework)

REST API pemesanan buku dengan Play Framework + MySQL.

## Best REST API practices yang diterapkan

- Versioning endpoint: `/api/v1/...`
- Resource naming: plural nouns (`/orders`)
- Layered architecture: `Controller -> Service -> Repository`
- Request validation terpisah (mirip Laravel FormRequest)
- Resource transformer untuk response (mirip Laravel API Resource)
- HTTP status code yang konsisten (`201`, `200`, `400`, `404`, `500`)
- Error response terstandar JSON (`code`, `message`, `details`)
- Pagination (`page`, `size`) untuk endpoint list
- Validasi input request body
- Health endpoint (`/api/v1/health`)
- Database migration dengan Play Evolutions

## Menjalankan aplikasi

1. Pastikan MySQL aktif.
2. Buat database:
   - `pemesanan_buku`
3. Atur environment variable (opsional):
   - `DB_URL`
   - `DB_USER`
   - `DB_PASSWORD`
4. Jalankan:

```bash
sbt run
```

API akan aktif di `http://localhost:9002`.

## Endpoint

- `GET /api/v1/health`
- `POST /api/v1/orders`
- `GET /api/v1/orders?page=1&size=10`
- `GET /api/v1/orders/:id`
- `PUT /api/v1/orders/:id`
- `DELETE /api/v1/orders/:id`

## Contoh request

### Create Order

```bash
curl -X POST http://localhost:9002/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Andi",
    "bookTitle": "Clean Code",
    "quantity": 2
  }'
```

Response sukses (contoh):

```json
{
  "success": true,
  "message": "Order created successfully",
  "data": {
    "id": 1,
    "customerName": "Andi",
    "bookTitle": "Clean Code",
    "quantity": 2,
    "orderedAt": "2026-04-14T09:00:00",
    "createdAt": "2026-04-14T09:00:00",
    "updatedAt": "2026-04-14T09:00:00"
  }
}
```

### List Orders

```bash
curl "http://localhost:9002/api/v1/orders?page=1&size=10"
```

Response list (contoh):

```json
{
  "success": true,
  "message": "Orders retrieved successfully",
  "data": [],
  "meta": {
    "page": 1,
    "size": 10,
    "totalItems": 0,
    "totalPages": 0
  }
}
```

### Get Order by ID

```bash
curl "http://localhost:9002/api/v1/orders/1"
```

### Update Order

```bash
curl -X PUT http://localhost:9002/api/v1/orders/1 \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Andi Pratama",
    "bookTitle": "Effective Scala",
    "quantity": 3
  }'
```

### Delete Order

```bash
curl -X DELETE http://localhost:9002/api/v1/orders/1
```

Response delete (contoh):

```json
{
  "success": true,
  "message": "Order deleted successfully"
}
```
