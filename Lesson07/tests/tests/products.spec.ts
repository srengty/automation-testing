import { test, expect } from '@playwright/test';

test('GET /products returns a list', async ({ request }) => {
    const res = await request.get('/products');

    expect(res.status()).toBe(200);
    expect(res.ok()).toBeTruthy();
    console.log(await res.text());
    const body = await res.json();
    expect(Array.isArray(body)).toBe(true);
});

test('POST creates a product, GET reads it back', async ({ request }) => {
    // CREATE
    const create = await request.post('/products', {
        data: { name: 'Keyboard', price: 49.9 },
    });
    expect(create.status()).toBe(201);

    const created = await create.json();
    expect(created).toMatchObject({ name: 'Keyboard', price: 49.9 });
    expect(created.id).toBeDefined();

    // READ BACK
    const read = await request.get(`/products/${created.id}`);
    expect(read.status()).toBe(200);
    expect(await read.json()).toMatchObject({ id: created.id });
});

// test('PATCH updates, DELETE removes', async ({ request }) => {
//     const { id } = await (await request.post('/products', {
//         data: { name: 'Mouse', price: 19 },
//     })).json();

//     // UPDATE
//     const upd = await request.patch(`/products/${id}`, {
//         data: { price: 15 },
//     });
//     expect(upd.status()).toBe(200);
//     expect((await upd.json()).price).toBe(15);

//     // DELETE
//     const del = await request.delete(`/products/${id}`);
//     expect(del.status()).toBe(200);

//     // VERIFY GONE
//     const gone = await request.get(`/products/${id}`);
//     expect(gone.status()).toBe(404);
// });