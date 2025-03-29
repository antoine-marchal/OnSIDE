# OnSIDE - Optimisation Script Integrated Development Environment

## Présentation

OnSIDE (Optimisation Script Integrated Development Environment) est une application développée en Java qui offre un environnement de développement intégré pour la création de scripts et de micro-applications. Ces scripts sont principalement destinés à résoudre des problèmes de manipulation de données et/ou des problèmes d'optimisation par contraintes.

L'application permet aux utilisateurs de développer des scripts exécutables directement dans l'environnement ou d'exporter ces scripts en micro-applications autonomes sous forme d'exécutables Windows.

## Compatibilité

OnSIDE est développé en Java, ce qui lui confère une compatibilité potentielle avec tous les systèmes d'exploitation disposant d'une machine virtuelle Java. Cependant, pour des raisons de compatibilité qui n'ont pas encore été résolues, le logiciel est actuellement limité au système d'exploitation Windows et requiert Java dans sa version 1.7 ou supérieure.

## Architecture du projet

Le projet est organisé en plusieurs packages principaux :

- **engine** : Contient les classes principales pour l'exécution des scripts (ScriptManager), le chiffrement (AES), l'exportation (Export) et la redirection de la console (TextAreaPrintStream).
- **outils** : Regroupe les classes utilitaires comme Parametres (constantes et configuration) et Utils (fonctions diverses).
- **ui** : Contient les classes d'interface utilisateur, notamment Main (point d'entrée), ModelBuild (pour les applications autonomes) et les différents panneaux de l'interface.

## Points d'entrée

Il existe trois points d'entrée dans le projet, situés dans les classes suivantes :

1. **Main** : Point d'entrée principal du logiciel. Une extraction en fichier .jar exécutable (en séparant le fichier jar des bibliothèques) permet de générer le fichier exec.jar, point d'entrée principal du logiciel.

2. **ModelBuild** : Point d'entrée pour l'exécution des exports de script sous format exécutable. Il doit être compilé en prenant soin d'y intégrer les bibliothèques du classpath vers data_lib/modelbuild.jar.

3. **Export** : Permet de transformer le fichier exec.jar en fichier OnSIDE.exe, exécutable principal du logiciel.

## Technologie des scripts

Les scripts développés sur OnSIDE sont des scripts Groovy. Pour simplifier le développement, une surcouche de Groovy a été implémentée pour faciliter l'accès aux fonctionnalités utiles dans un script d'optimisation. Ces fonctions sont cataloguées dans la bibliothèque du logiciel.

Cette surcouche est en réalité un script Groovy qui précède de façon invisible le script développé par l'utilisateur. Il est ainsi possible de développer des « modules » afin d'enrichir une série de scripts utilisant des fonctions parfois complexes.

## Modules disponibles

OnSIDE propose plusieurs modules prédéfinis pour faciliter le développement :

- **console** : Fonctions d'affichage et d'interaction avec la console
- **csp** : Fonctions pour la programmation par contraintes (Constraint Satisfaction Problem)
- **donnees** : Fonctions de manipulation de données
- **saisies** : Fonctions pour la saisie et l'interaction utilisateur
- **utils** : Fonctions utilitaires diverses

## Fonctionnalités principales

Le logiciel offre un ensemble de fonctionnalités centrées sur le développement de scripts :

- **Création/Édition de scripts** : Interface complète pour créer, ouvrir et éditer des scripts
- **Exécution intégrée** : Possibilité de lancer les scripts directement dans l'environnement
- **Exportation** : Conversion des scripts en applications autonomes (.exe) exécutables nativement sous Windows
- **Assistance au développement** : Auto-complétion pour faciliter l'écriture de code
- **Bibliothèque de fonctions** : Ensemble de fonctions prédéfinies pour :
  - La manipulation des données
  - L'importation et l'exportation de données
  - La création d'interactions utilisateur
  - L'implémentation et la résolution de problèmes de satisfaction de contraintes
  - La gestion de threads et leur utilisation
- **Sécurité** : Protection de tout ou partie du script par chiffrement AES
- **Extensibilité** : Possibilité pour les développeurs de créer des modules utilisables par des tiers

## Bibliothèques utilisées

OnSIDE s'appuie sur plusieurs bibliothèques externes pour son fonctionnement :

- **Launch4j** : Transforme une application Java (.jar) en exécutable Windows (.exe)
  - Site : http://launch4j.sourceforge.net/
- **JaCoP** : Moteur de résolution de programmation par contraintes
  - Site : http://jacop.osolpro.com/
- **Groovy** : Surcouche de Java offrant un langage orienté objet plus souple et dynamique
  - Site : http://groovy.codehaus.org/Embedding+Groovy
- **RSyntaxTextArea** : Éditeur de code compatible avec le langage Groovy
  - Site : http://fifesoft.com/rsyntaxtextarea/
- **TrueZIP** : Manipulation de fichiers archives
- **Apache Commons** : Diverses bibliothèques utilitaires
- **Zip4j** : Manipulation de fichiers ZIP

## Principe et utilisation

OnSIDE a été développé pour répondre à des besoins spécifiques : fournir un moyen de créer et de résoudre des problèmes de programmation par contraintes, et proposer des outils clé en main de type « press button ».

L'application constitue un support viable et complémentaire aux outils utilisés dans le domaine de l'optimisation tels que R et JMP, tout en offrant une ouverture sur le domaine de la programmation par contraintes.

## Sécurité

OnSIDE intègre des fonctionnalités de sécurité permettant de protéger les scripts développés via un chiffrement AES. Cette fonctionnalité est particulièrement utile pour protéger la propriété intellectuelle des scripts ou pour sécuriser des informations sensibles.

## Historique et versions

Le logiciel a été développé initialement en 2014 par Antoine Marchal. Bien qu'il soit toujours utilisé et maintenu, seuls de légers ajouts ont été réalisés depuis la première version. La version actuelle est la 1.8.

## Contact

Pour toute information complémentaire, vous pouvez contacter le développeur principal :
- Antoine Marchal : antoine.marchal@pm.me
