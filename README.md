# OnSIDE

OnSIDE ou Optimisation Script Integrated Development Environment est une application développée
sous JAVA qui représente un réel support de développement de scripts et de micro-applications visant
à résoudre des problèmes de manipulation de données et/ou des problèmes d'optimisation par
contraintes.

OnSIDE est donc avant tout un IDE offrant la possibilité à l'utilisateur de développer des scripts
exécutables à l'aide de l'application ou d'exporter ces scripts en micro-applications autonomes.

OnSIDE est développé sous JAVA, ce qui peut lui valoir une compatibilité avec tous les systèmes d’exploitation comportant la machine virtuelle JAVA.
Malgré cela, pour des soucis de compatibilité qui ne sont pas encore traités, le logiciel est restreint au système d’exploitation Windows et à JAVA dans sa version 1.7+.

# Librairies

OnSIDE embarque tout un tas de librairies participent à son fonctionnement, on retrouve alors :

*	Launch4j : Une librairie qui transforme une application JAVA distribuée comme un .jar exécutable en une application exécutable nativement par Windows.
http://launch4j.sourceforge.net/
*	JaCoP : Une librairie qui apporte un moteur de résolution de programmation par contraintes.
http://jacop.osolpro.com/
*	Groovy : C’est une surcouche de JAVA qui permet de fournir un autre langage orienté objet, plus souple. C’est un langage dynamique avec des fonctionnalités similaires à celles de langages comme Python, Ruby ou Perl. Un script Groovy peut être interprété ou, ici, compilé de façon dynamique en du bytecode JAVA.
http://groovy.codehaus.org/Embedding+Groovy
*	RSyntaxTextArea : Cette librairie fournit un éditeur de code compatible avec le langage Groovy.
http://fifesoft.com/rsyntaxtextarea/

# Compilation

Il existe trois points d'entrée dans le projet situées dans les classes Main, ModelBuild et Export.
* Le point d'entrée de la classe Main correspond à l'execution principale du logiciel. Une extraction en fichier .jar executable (en prenant soin de séparer le fichier jar des librairies) permet de générer le fichier exec.jar, point d'entrée principale du logiciel.
* Le point d'entrée de la classe ModelBuild correspond à l'execution des exports de script sous format executable. Il doit etre compilé en prenant le soin d'y intégrer les librairies du classpath vers data_lib/modelbuild.jar.
* Le point d'entrée de la classe Export permet de transformer le fichier exec.jar en fichier OnSIDE.exe, executable principal du logiciel.


## Les scripts 

Les scripts développés sur OnSIDE sont donc des scripts Groovy.
En revanche, en vue de simplifier au maximum le développement de ces scripts, une surcouche de Groovy a été développée pour simplifier l’accès aux fonctionnalités utiles que l’on voudrait avoir dans un script d’optimisation.
Ces fonctions sont cataloguées dans la bibliothèque du logiciel.
Cette surcouche est en réalité un script Groovy qui précède de façon invisible le script développé par l’utilisateur.
Il est ainsi possible de développer des « modules » afin d’agrémenter une série de scripts utilisant des fonctions parfois compliquées à expliciter.

## Fonctionalités

Le logiciel offre un ensemble de fonctionnalités tournant autour du Scripting mais aussi à l'aide au
développement en plus de quelques autres fonctionnalités.
On retrouve notamment la possibilité de :
* Créer/Ouvrir/Editer un nouveau script
* Lancer ce script directement à travers le logiciel
* Exporter ce script en application autonome sous le format exécutable nativement par Windows (.exe)
* Permettre l'auto complétion dans le développement d'un script
* Réunir une bibliothèque de fonctions utiles à :
	* La manipulation des données
	* L'importation et l'exportation de ces données
	* La création d'interactions entre l'utilisateur et le script une fois lancé
	* L'implémentation et la résolution d'un problème de satisfactions de contraintes
	* La création d'un pool de thread et à son utilisation
* Protéger tout ou une partie du script par chiffrage AES
* Devenir évolutif en autorisant les développeurs à créer des modules pouvant être utilisés par des tiers

## Principe 

Ce logiciel a été développé à travers des demandes qui étaient à la fois de fournir un moyen de créer
et de résoudre des problèmes de programmation par contraintes et de fournir des outils clé en mains
de type« press button ».
Finalement, OnSIDE est un support viable et complémentaire aux outils utilisés dans ce domaine tels
que R et JMP et offre par la même, une ouverture sur le domaine de la programmation par contraintes.

# Version

Le logiciel a été développé en 2014. Bien qu'il me soit toujours utile, de faibles ajouts ont été réalisés depuis la première version.
