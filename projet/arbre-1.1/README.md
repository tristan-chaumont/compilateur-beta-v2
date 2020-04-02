# Projet REFLECTION

Ce projet correspond aux démos du chapitre "Réflexivité"  de mon cours de programmation Avancée en Master MIAGE de l'Université de Lorraine.

## Exemple
Il comporte 2 démos :

###Inspection d'une classe

````console
$ ./inspect -c fr.ul.miage.reflection.Patronyme
Inspection de la classe: fr.ul.miage.reflection.Patronyme 
héritée de : class java.lang.Object 
Attributs :
	 -id 
	 -nom 
	 -prenom 
Methodes :
	 -getId 
	 -setId 
	 -getNom 
	 -setNom 
	 -getPrenom 
	 -setPrenom 
	 -getInitiales 
$ 
````

### Invocation d'une méthode

````console
$ ./invoke -c fr.ul.miage.reflection.Patronyme -m getInitiales
Classe: fr.ul.miage.reflection.Patronyme
Methode: getInitiales
Resultat: AR
$ 
````



## Auteur

Azim Roussanaly

## Licence

Ce projet est distribué sous une licence MIT
(voir [license.md](license.md) pour plus de détails)

