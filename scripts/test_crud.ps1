# Complete CRUD Test Suite
Write-Output "=== CRUD TEST SUITE ==="
Write-Output ""

# 1. CREATE
Write-Output "1. CREATE - POST new product"
$newProduct = @{
    id = 9999
    name = "Test Product"
    description = "Test Description"
    price = 99.99
    category = "Test"
    stockQuantity = 10
} | ConvertTo-Json

try {
    $createRes = Invoke-RestMethod -Uri 'http://localhost:8080/api/products/addProduct' -Method Post -ContentType 'application/json' -Body $newProduct -ErrorAction Stop
    Write-Output "[PASS] CREATE: $createRes"
} catch {
    Write-Output "[FAIL] CREATE: $($_.Exception.Message)"
    exit 1
}

Write-Output ""

# 2. READ ALL
Write-Output "2. READ ALL - GET /api/products"
try {
    $allProducts = Invoke-RestMethod -Uri 'http://localhost:8080/api/products' -Method Get -ErrorAction Stop
    $count = if ($allProducts -is [array]) { $allProducts.Count } else { 1 }
    Write-Output "[PASS] READ ALL: Found $count total products"
} catch {
    Write-Output "[FAIL] READ ALL: $($_.Exception.Message)"
    exit 1
}

Write-Output ""

# 3. READ BY ID
Write-Output "3. READ BY ID - GET /api/products/9999"
try {
    $singleProduct = Invoke-RestMethod -Uri 'http://localhost:8080/api/products/9999' -Method Get -ErrorAction Stop
    Write-Output "[PASS] READ BY ID: Retrieved '$($singleProduct.name)' - Price: $($singleProduct.price)"
} catch {
    Write-Output "[FAIL] READ BY ID: $($_.Exception.Message)"
    exit 1
}

Write-Output ""

# 4. UPDATE
Write-Output "4. UPDATE - PUT /api/products/9999"
$updateProduct = @{
    id = 9999
    name = "Updated Test Product"
    description = "Updated Description"
    price = 149.99
    category = "Updated"
    stockQuantity = 20
} | ConvertTo-Json

try {
    $updateRes = Invoke-RestMethod -Uri 'http://localhost:8080/api/products/9999' -Method Put -ContentType 'application/json' -Body $updateProduct -ErrorAction Stop
    Write-Output "[PASS] UPDATE: $updateRes"
} catch {
    Write-Output "[FAIL] UPDATE: $($_.Exception.Message)"
    exit 1
}

# Verify update
Write-Output ""
Write-Output "4b. VERIFY UPDATE - GET /api/products/9999"
try {
    $verifyUpdate = Invoke-RestMethod -Uri 'http://localhost:8080/api/products/9999' -Method Get -ErrorAction Stop
    Write-Output "[PASS] VERIFY UPDATE: Name changed to '$($verifyUpdate.name)' - Price: $($verifyUpdate.price)"
} catch {
    Write-Output "[FAIL] VERIFY UPDATE: $($_.Exception.Message)"
    exit 1
}

Write-Output ""

# 5. DELETE
Write-Output "5. DELETE - DELETE /api/products/9999"
try {
    $deleteRes = Invoke-RestMethod -Uri 'http://localhost:8080/api/products/9999' -Method Delete -ErrorAction Stop
    Write-Output "[PASS] DELETE: $deleteRes"
} catch {
    Write-Output "[FAIL] DELETE: $($_.Exception.Message)"
    exit 1
}

Write-Output ""

# 6. VERIFY DELETE
Write-Output "6. VERIFY DELETE - GET /api/products/9999 (should be 404)"
try {
    Invoke-RestMethod -Uri 'http://localhost:8080/api/products/9999' -Method Get -ErrorAction Stop
    Write-Output "[FAIL] VERIFY DELETE: Product still exists"
    exit 1
} catch {
    if ($_.Exception.Response.StatusCode -eq 404) {
        Write-Output "[PASS] VERIFY DELETE: Product confirmed deleted (404 Not Found)"
    } else {
        Write-Output "[FAIL] VERIFY DELETE: Unexpected status $($_.Exception.Response.StatusCode)"
        exit 1
    }
}

Write-Output ""
Write-Output "=========================================="
Write-Output "[SUCCESS] ALL CRUD TESTS PASSED"
Write-Output "=========================================="
