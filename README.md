# Minispec

Minispec est un langage de modélisation pour générer du code.

## Membres

- Christophe Garcia
- Louis Dosnon
- Jonathan Bayle
- Marie Le Buhan
- Alexia Sorin

## Ce qui est fait

- 2. La version minimale pour démarrer
- 3. Associations unidirectionnelles entre entités
  - Prise en charge des tableaux et des collections (List, Set, Bag)
  - Si on prend en charge un bag, ont crée une ArrayList (Bag n'existant pas)
  - Possibilité de faire des imbrications de tableaux et/ou collections
- 4. Héritage et Modèle
  - Prise en compte des dépendances circulaires, si dépendance une erreur est renvoyée
  - Une entité ne peut ajouter un attribut dont le nom est déjà utilisé chez lui-même ou chez un de ses parents (Héritage), sinon une erreur est renvoyée
- 5. Valeurs initiales des attributs
- 6. Lot d'instances :
  - Sérialisation/Matérialisation des instances dans un XML à part
  - Ajout de la méthode valueOf pour chaque entité
- 7. Rebouclage :
  - XML du méta-modèle
  - Concept d'interface (basique)
  - Concept d'énumération

## Les améliorations à faire

- 4. Héritage et modèle :
  - si une entité ajoute un attribut après la création de entités qui l'héritent, et que une de ces entités a déjà un attibut du même nom, alors les deux sont présents
- 6. Lot d'instances :
  - permettre la sérialisation des collections/tableaux
- 7. Rebouclage :
  - ajout du pattern visiteur
  - utilisation du méta-modèle créé

## Autres informations

- Mise en majusucule de la première lettre des classes
- Nommage des getter et setter en PascalCase (ex: "getMonAttribut" et non "getmonAttribut")
- Mise en miniscule des noms de packages (conventions Java)
- Initialisation par défaut des collections et tableaux
- Import par défaut des collections, surcharge par le XML des primitives (définition personnalisée des imports)
