## COMPILATEUR BETA
* Tristan Chaumont
* Alexandre Da Siva Carmo
* Vincent Dahlem
* Quentin Noirot

> Création d'un compilateur pouvant traduire un langage simple en assembleur [bêta](https://computationstructures.org/notes/pdfs/beta.pdf).

## Algorithmes utilisés
##### genererProgramme() 
```
→ noeud: noeud principal de l’arbre abstrait
→ tds: table des symboles
← res: code asm

debut
	res ← “.include beta.uasm”
  	res +← “.include intio.asm”
  	res +← “.options tty” 
  	res +← “CMOVE(pile, SP)”
  	res +← “BR(debut)”
  	res +← genererData(tds)
  	res +← genererCode(noeud)
  	res +← “debut:”
  	res +← “CALL(main)”
  	res +← “HALT()”
  	res +← “pile:”
fin
```

##### genererData()
```
→ tds: table des symboles
← res: code asm

debut
	res ← “”
	pour chaque e dans tds
		init ← 0
		si e.type = int et e.scope = global et e.cat != fonction alors
			si e.val existe alors
				init ← val
			fsi
			res +← e.nom + “:” + “LONG(” + init + “)”
		fsi
	fpour
fin
```

##### genererCode()
```
→ noeud: noeud principal de l’arbre abstrait
→ tds: table des symboles
← res: code asm

debut
	res ← “”
	pour chaque fils f dans noeud faire
		res +← genererFonction(f, tds) 	
	fpour
fin
```

##### genererFonction()
```
→ fonction: noeud de l’arbre abstrait
→ tds: table des symboles
← res: code asm

debut
	res ← fonction.nom + “:”
  	res +← “PUSH(LP)”
  	res +← “PUSH(BP)”
  	res +← “MOVE(SP, BP)”
  	res +← “ALLOCATE(” + tds.rechercher(fonction.nom, global).nbLoc + “)”
  	pour chaque fils f dans fonction faire
		res +← genererInstruction(f, tds)
	fpour
  	res +← “MOVE(BP,  SP)”
  	res +← “POP(BP)”
  	res +← “POP(LP)”
  	res +← “RTN()”
fin
```

##### genererBloc()
```
→ bloc: noeud de l’arbre abstrait
→ tds: table des symboles
← res: code asm

debut
	res ← “”
	pour chaque fils f dans bloc faire
		res +← genererInstruction(f, tds)
	fpour
fin
```

##### genererInstruction()
```
→ noeud: noeud de l’arbre abstrait
→ tds: table des symboles
← res: code asm

debut
	res ← “”
	cas où noeud.cat est une affectation :
		res +← genererAffectation(noeud, tds)
	
	cas où noeud.cat est une écriture :
		res +← genererEcrire(noeud, tds)

	cas où noeud.cat est une condition SI :
		res +← genererSi(noeud, tds)

	cas où noeud.cat est une boucle TANT QUE :
		res +← genererTantQue(noeud, tds)

	cas où noeud.cat est un retour de fonction :
		res +← genererRetourne(noeud, tds)

	cas où noeud.cat est un appel de fonction :
		res +← genererExpression(noeud, tds)
fin
```

##### genererExpression()
```
→ noeud: noeud de l’arbre abstrait
→ tds: table des symboles
← res: code asm

debut
	res ← “”
	cas où noeud.cat est une constante :
		res +← “CMOVE(” + noeud.val + “)”
		res +← “PUSH(r0)
	
	cas où noeud.cat est un idf :
		symbole ← tds.rechercher(noeud, noeud.scope)
		cas où noeud.scope = global :
			res +← “LD(“ + idf.val + “, r0)”
			res +← “PUSH(r0)”
		cas où noeud.scope = local :
			res +← “GETFRAME(“ + (symbole.rang * 4) + “, r0)”
      			res +← “PUSH(r0)
    		cas où noeud.scope = param :
			res +← “GETFRAME(“ + ((symbole.rang + 3) * (-4)) + “, r0)”
	    		res +← “PUSH(r0)”

	cas où noeud.cat est une opération :
		res +← genererExpression(fg(noeud), tds)
		res +← genererExpression(fd(noeud), tds)
		res +← “POP(r2)”
    		res +← “POP(r1)”
    		res +← “ADD(r1, r2, r3)”   [ou SUB, MUL, DIV]
    		res +← “PUSH(r3)”

	cas où noeud.cat est une lecture :
		res +← “RDINT()”
		res +← “PUSH(r0)”

	cas où noeud.cat est un appel de fonction :
		res +← genererAppel(noeud, tds)
fin
```

##### genererExpressionBooléenne()
```
→ noeud: noeud de l’arbre abstrait
→ tds: table des symboles
← res: code asm

debut
	res ← “”
	cas où noeud.cat est l’opération ‘>’   [ ou ‘>=’ ] :
		res +← genererExpression(fg(noeud), tds)
    		res +← genererExpression(fd(noeud), tds)
		res +← “POP(r2)”
    		res +← “POP(r1)”
    		res +← “CMPLT(r2, r1, r3)”  [ou CMPLE(r2, r1, r3) si ‘>=’]
    		res +← “PUSH(r3)”

  	cas où noeud.cat est l’opération ‘<’   [ ou ‘<=’ ] :
		res +← genererExpression(fg(noeud), tds)
    		res +← genererExpression(fd(noeud), tds)
		res +← “POP(r2)”
    		res +← “POP(r1)”
    		res +← “CMPLT(r1, r2, r3)”  [ou CMPLE(r1, r2, r3) si ‘<=’]
    		res +← “PUSH(r3)”

  	cas où noeud.cat est l’opération ‘=’ :
		res +← genererExpression(fg(noeud), tds)
    		res +← genererExpression(fd(noeud), tds)
		res +← “POP(r2)”
    		res +← “POP(r1)”
	    	res +← “CMPEQ(r1, r2, r3)”
    		res +← “PUSH(r3)”

  	cas où noeud.cat est l’opération ‘!=’ :
		res +← genererExpression(fg(noeud), tds)
    		res +← genererExpression(fd(noeud), tds)
		res +← “POP(r2)”
    		res +← “POP(r1)”
    		res +← “CMPEQ(r1, r2, r3)”
    		res +← “CMPEQC(r3, 0, r4)”
    		res +← “PUSH(r4)”
fin
```

##### genererAffectation()
```
→ affectation: noeud de l’arbre abstrait
→ tds: table des symboles
← res: code asm

debut
	res ← genererExpression(fd(affectation), tds)
	res +← “POP(r0)”
	idf ← fg(affectation)
	symbole ← tds.rechercher(idf.val, idf.scope)
	cas où symbole.cat = global :
		res +← “ST(r0, “ + idf.val + “)”
	cas où symbole.cat = local
		res +← “PUTFRAME(r0, “ + (symbole.rang * 4) +”)”
	cas où symbole.cat = param
    	res +← “PUTFRAME(r0, “ + ((symbole.rang + 3) * (-4)) +”)”
fin
```

##### genererEcrire()
```
→ ecrire: noeud de l’arbre abstrait
→ tds: table des symboles
← res: code asm

debut
	res ← genererExpression(fils(ecrire), tds)
	res +← “POP(r0)”
	res +← “WRINT()”
fin
```

##### genererSi()
```
→ si: noeud de l’arbre abstrait
→ tds: table des symboles
← res: code asm

debut
	res ← genererExpressionBooléenne(fils0(si), tds)
	res +← “POP(r0)”
	res +← “BF(r0, sinon” + si.# + “)”
	res +← genererBloc(fils1(si), tds)
	res +← “BR(fsi” + si.# + “)”
	res +← “sinon” + si.# + “:”
	res +← genererBloc(fils2(si), tds)
	res +← “fsi” + si.# + “:”
fin
```

##### genererTantQue()
```
→ tantque: noeud de l’arbre abstrait 
→ tds: table des symboles
← res: code asm

debut
	res ← “tantque” + tantque.# + “:”
	res +←  genererExpressionBooléenne(fils0(tantque), tds)
	res +← “POP(R0)”
  	res +← “BF(r0, ftantque” + tantque.# + “)”
  	res +← genererBloc(fils1(tantque), tds)
  	res +← “BR(tantque” + tantque.# + “)”
  	res +← “ftantque” + tantque.#  + “:”
fin
```

##### genererAppel()
```
→ appel: noeud de l’arbre abstrait 
→ tds: table des symboles
← res: code asm

debut
	fonction ← appel.val
	symbole ← tds.rechercher(fonction.nom, global)
	res ← “”
	si symbole.type  = entier alors
		res +← “ALLOCATE(1)”
	fsi
	pour chaque fils paramètre appel faire
		res +← genererExpression(paramètre, tds)
	fpour
	res +← “CALL(“ + fonction.nom + “, ” + symbole.nbParam + “)”
fin
```

##### genererRetourne()
```
→ retourne : noeud de l’arbre abstrait 
→ tds: table des symboles
← res: code asm

debut
	res ← genererExpression(fils(retourne), tds)
	res +← “POP(r0)”
	fonction ← retourne.val
	symbole ← tds.rechercher(fonction.nom, global)
	res +← “PUTFRAME(r0,” + ((3 + symbole.nbParam) * (-4)) + “)”
fin
```
