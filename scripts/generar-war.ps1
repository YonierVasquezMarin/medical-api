$ErrorActionPreference = "Stop"

function RutaJdkEsValida([string] $ruta) {
	if ([string]::IsNullOrWhiteSpace($ruta)) {
		return $false
	}
	return Test-Path (Join-Path $ruta "bin\java.exe")
}

function ObtenerRutaDelJdk() {
	$raices = @(
		"${env:ProgramFiles}\Eclipse Adoptium",
		"${env:ProgramFiles}\Java",
		"${env:ProgramFiles}\Microsoft",
		"${env:ProgramFiles}\Amazon Corretto"
	)
	return Get-ChildItem -Path $raices -Directory -ErrorAction SilentlyContinue |
		Where-Object { RutaJdkEsValida $_.FullName } |
		Where-Object { $_.Name -match "21" -or $_.Name -eq "latest" } |
		Sort-Object Name -Descending |
		Select-Object -ExpandProperty FullName |
		Select-Object -First 1
}

$javaHome = $env:JAVA_HOME
if (-not (RutaJdkEsValida $javaHome)) {
	$javaHome = ObtenerRutaDelJdk
}

if (-not (RutaJdkEsValida $javaHome)) {
	Write-Error "No se encontro un JDK 21. Instale Temurin 21 o defina JAVA_HOME apuntando al JDK (no a javapath)."
	exit 1
}

$env:JAVA_HOME = $javaHome
Write-Host "JAVA_HOME=$env:JAVA_HOME"

$directorioProyecto = Split-Path -Parent $PSScriptRoot
$wrapper = Join-Path $directorioProyecto "mvnw.cmd"
& $wrapper clean package -DskipTests
exit $LASTEXITCODE
