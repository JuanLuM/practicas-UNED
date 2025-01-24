module StackMachine where

-- TIPOS DE DATOS --

type Stack a = [a]

data BOp = ADD | SUB | MUL | DIV
  deriving (Eq, Read, Show)

data UOp = NEG
  deriving (Eq, Read, Show)

data SynTree a = Binary BOp (SynTree a) (SynTree a) | Unary UOp (SynTree a) | Operand a
  deriving (Eq, Read, Show)

-- FUNCIONES AUXILIARES PARA MANEJO DE LA PILA --
-------------------------------------------------

-- push recibe un elemento de un tipo a y una pila de elementos de tipo a
-- y devuelve la pila con el elemnto apilado
push :: a -> Stack a -> Stack a
push elemento pila = elemento:pila

-- pop recibe una pila de elementos de tipo a y la devuelve sin la cima.
pop :: Stack a -> Stack a
pop (x:xs) = xs

-- top recibe una pila de elementos de tipo a y devuelve la cima.
top :: Stack a -> a
top (x:xs) = x

-- CONSTRUCCION DEL ARBOL SINTACTICO --

createSynTree :: [String] -> SynTree Integer
createSynTree tree_words = createSynTreeAux (reverse tree_words) [] -- invierto el orden para asegurarme de tener los operandos antes de los operadores
  where
    createSynTreeAux [] stack = top stack  -- Cuando llegue al final de la lista, devuelve la cima de la pila que contiene la raiz del árbol
    createSynTreeAux (x:xs) stack
      | x == "+" = createSynTreeAux xs (push (Binary ADD child1 child2) output_stack) -- añade a la pila el nodo correspondiente, cogiendo los hijos creados y apilados anteriormente.
      | x == "-" = createSynTreeAux xs (push (Binary SUB child1 child2) output_stack) -- añade a la pila el nodo correspondiente, cogiendo los hijos creados y apilados anteriormente.
      | x == "*" = createSynTreeAux xs (push (Binary MUL child1 child2) output_stack) -- añade a la pila el nodo correspondiente, cogiendo los hijos creados y apilados anteriormente.
      | x == "/" = createSynTreeAux xs (push (Binary DIV child1 child2) output_stack) -- añade a la pila el nodo correspondiente, cogiendo los hijos creados y apilados anteriormente.
      | x == "N" = createSynTreeAux xs (push (Unary NEG child1) stack2)               -- añade a la pila el nodo correspondiente, cogiendo los hijos creados y apilados anteriormente.
      | otherwise = createSynTreeAux xs (push (Operand (read x :: Integer)) stack)    -- añade a la pila el operando para poder usarlo más adelante cuando encontremos un operador.
        where                            -- | Este where solo lo añado por claridad en el código y
          child1 = top stack             -- | porque la pila sea, conceptualmente lo más parecido a
          stack2 = pop stack             -- | una pila de verdad, además de por desapilar cada elemento
          child2 = top stack2            -- | en el orden correcto. Esto mismo se puede hacer directamente
          output_stack = pop stack2      -- | en la función usando funciones para manejar listas como drop o head

-- EVALUACION --

-- En esta funcion no hago uso de una pila, porque aunque creo que en haskell la recursividad
-- se gestiona de otra manera el resultado debería ser equivalente, y en otros paradigmas, la
-- la recursividad ya se gestiona de manera natural con una pila. No obstante, al final del
-- archivo hago otra implementación (que dejo comentada) de evalSynTree haciendo uso de una pila.

evalSynTree :: SynTree Integer -> Integer
evalSynTree (Binary ADD child1 child2) = evalSynTree child1 + evalSynTree child2
evalSynTree (Binary SUB child1 child2) = evalSynTree child1 - evalSynTree child2
evalSynTree (Binary MUL child1 child2) = evalSynTree child1 * evalSynTree child2
evalSynTree (Binary DIV child1 child2) = div (evalSynTree child1) (evalSynTree child2)
evalSynTree (Unary NEG child) = - evalSynTree child
evalSynTree (Operand integer) = integer

----------------------------------------------------------------------------------
-- IMPLEMENTACIONES ALTERNATIVAS (EVALUACIÓN CON PILA Y CREACIÓN HACIA DELANTE) --
-----------------------------------------------------------------------------------
-- Se pueden comentar las anteriores y descomentar las siguientes para probarlas --
------------------------------------------------------------------------------------------------------
-- Las siguientes funciones las he creado pensando que la práctica te pedía programar las funciones --
-- de una manera concreta, no obstante, aunque su definición es distinta, el resultado es el mismo. --
------------------------------------------------------------------------------------------------------

-- CONSTRUCCION DEL ARBOL SINTACTICO --

-- createSynTree :: [String] -> SynTree Integer
-- createSynTree (x:xs)
--   | x == "+" = Binary ADD (createSynTree xs)(createSynTree (nextSynTreeString xs 1) )
--   | x == "-" = Binary SUB (createSynTree xs)(createSynTree (nextSynTreeString xs 1) )
--   | x == "*" = Binary MUL (createSynTree xs)(createSynTree (nextSynTreeString xs 1) )
--   | x == "/" = Binary DIV (createSynTree xs)(createSynTree (nextSynTreeString xs 1) )
--   | x == "N" = Unary NEG (createSynTree xs)
--   | otherwise = Operand (read x :: Integer)
--   where
--     nextSynTreeString :: [String] -> Integer -> [String]  -- A esta función le paso una lista con los operandos y operadores y el número de operandos que falta para completar el árbol, y
--     nextSynTreeString (x:xs) operandsPreviousTree         -- devuelve el resto de simbolos que ya pertenecen al siguiente árbol, esto lo hago para no invertir los símbolos.
--       | operandsPreviousTree == 0 = x:xs                  -- Aquí si no hay que buscar más operandos, devuelve la lista.
--       | x `elem` ["+","-","*","/"] = nextSynTreeString xs (operandsPreviousTree + 1)
--       | x == "N" = nextSynTreeString xs operandsPreviousTree
--       | otherwise = nextSynTreeString xs (operandsPreviousTree - 1)

-- EVALUACION --

-- evalSynTree :: SynTree Integer -> Integer
-- evalSynTree synTree = top (evalSynTreeAux synTree [])
--   where
--     evalSynTreeAux (Binary op child1 child2) stack =
--       case op of
--         ADD -> push (operand1 + operand2) output_stack
--         SUB -> push (operand1 - operand2) output_stack
--         MUL -> push (operand1 * operand2) output_stack
--         DIV -> push (operand1 `div` operand2) output_stack
--       where
--         evaluated_child1_stack = evalSynTreeAux child1 stack
--         evaluated_child2_stack = evalSynTreeAux child2 evaluated_child1_stack
--         operand2 = top evaluated_child2_stack
--         top_less_stack = pop evaluated_child2_stack
--         operand1 = top top_less_stack
--         output_stack = pop top_less_stack
--     evalSynTreeAux (Unary NEG child) stack = push (- operand) output_stack     -- | En este patrón, stack y output_stack
--       where                                                                    -- | son iguales pero uso output_stack
--         evaluated_child_stack = evalSynTreeAux child stack                     -- | para que se vea como desapilo el resultado
--         operand = top evaluated_child_stack                                    -- | de evaluar el árbol hijo y se vea una
--         output_stack = pop evaluated_child_stack                               -- | pila conceptualmente correcta.
--     evalSynTreeAux (Operand integer) stack = push integer stack

