$jsonPath = "products.json"
$json = Get-Content $jsonPath -Raw
try {
    $res = Invoke-RestMethod -Uri 'http://localhost:8080/api/products/bulk' -Method Post -ContentType 'application/json' -Body $json -ErrorAction Stop
    $res | ConvertTo-Json -Depth 5 | Out-File posted_result.json -Encoding utf8
    Write-Output "POST succeeded. Saved response to posted_result.json"
} catch {
    Write-Output "POST failed: $($_.Exception.Message)"
}

try {
    $all = Invoke-RestMethod -Uri 'http://localhost:8080/api/products' -Method Get -ErrorAction Stop
    $all | ConvertTo-Json -Depth 5 | Out-File get_all_response.json -Encoding utf8
    Write-Output "GET succeeded. Saved response to get_all_response.json"
} catch {
    Write-Output "GET failed: $($_.Exception.Message)"
}
