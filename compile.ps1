# === CONFIGURATION ===
$src = "src"
$lib = "exec_lib"
$out = "out"
$jarName = "exec.jar"
$mainClass = "ui.Main"
$manifest = "manifest.txt"
$maxLineLength = 70

# === CLEANUP ===
Remove-Item $out -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item $jarName -Force -ErrorAction SilentlyContinue
Remove-Item $manifest -Force -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Path $out | Out-Null

# === COMPILATION ===
Write-Host "[1/3] Compilation des sources..."
Get-ChildItem -Recurse -Filter *.java -Path $src | ForEach-Object { $_.FullName } | Out-File -Encoding ascii sources.txt
& javac -d $out -cp "$lib/*" "@sources.txt"
if ($LASTEXITCODE -ne 0) {
    Write-Host "[✗] Compilation échouée."
    Remove-Item sources.txt
    exit 1
}
Remove-Item sources.txt

# === MANIFEST GENERATION ===
Write-Host "[2/3] Génération du manifest avec pliage..."

# Préparer les JARs du classpath
$jarFiles = Get-ChildItem "$lib" -Filter *.jar
$classPath = ($jarFiles | ForEach-Object { "exec_lib/$($_.Name)" }) -join " "

# Fonction pour plier la ligne proprement
function Fold-Line {
    param([string]$line, [int]$max)
    $folded = @()
    while ($line.Length -gt $max) {
        $folded += $line.Substring(0, $max)
        $line = " " + $line.Substring($max)  # Continuation line starts with a space
    }
    $folded += $line
    return $folded
}

# Écriture du manifest avec pliage du Class-Path
Set-Content $manifest "Manifest-Version: 1.0"
Add-Content $manifest "Main-Class: $mainClass"
$foldedClassPath = Fold-Line "Class-Path: $classPath" $maxLineLength
$foldedClassPath | Add-Content $manifest
Add-Content $manifest ""  # Ligne vide de fin obligatoire

# === CREATION DU JAR ===
Write-Host "[3/3] Création de $jarName..."
& jar cfm $jarName $manifest -C $out .
java -cp $jarName";"$lib"/*" engine.Export
# === NETTOYAGE ===
Write-Host "[Nettoyage] Suppression de $out\..."
Remove-Item $out -Recurse -Force
Remove-Item $jarName
Remove-Item $manifest 
Write-Host "[✓] Build terminé avec succès. Lance : java -jar $jarName"
