module Main where

import System.IO

data BOp = ADD | SUB | MUL | DIV
  deriving (Eq, Read, Show)

data UOp = NEG
  deriving (Eq, Read, Show)

data SynTree a = Binary BOp (SynTree a) (SynTree a) | Unary UOp (SynTree a) | Operand a
  deriving (Eq, Read, Show)

-- VISUALIZACION DEL ARBOL SINTACTICO --

--viewSynTree :: (Show a) => SynTree a -> String
viewSynTree t = "\\documentclass{standalone}\n\\usepackage{tikz, tikz-qtree}\n\\begin{document}\n\\Tree" ++ (toLatex t) ++ "\n\\end{document}\n"
  where toLatex (Binary ADD t1 t2) = " [.+" ++ (toLatex t1) ++ (toLatex t2) ++ " ]"
        toLatex (Binary SUB t1 t2) = " [.-" ++ (toLatex t1) ++ (toLatex t2) ++ " ]"
        toLatex (Binary MUL t1 t2) = " [.*" ++ (toLatex t1) ++ (toLatex t2) ++ " ]"
        toLatex (Binary DIV t1 t2) = " [./" ++ (toLatex t1) ++ (toLatex t2) ++ " ]"
        toLatex (Unary  NEG t1)    = " [.N" ++ (toLatex t1) ++ " ]"
        toLatex (Operand n)        = " [." ++ show n ++ " ]"

-- "BUCLE" PRINCIPAL --
mainLoop :: IO ()
mainLoop = do { putStr "\nEscriba un árbol para ser convertido a LaTeX\narbol: ";
                hFlush stdout;
                e <- getLine;
                if ( e /= "" )
                  then let st = (read e :: (SynTree Integer)) in do {
                         putStr ("El código LaTeX para visualizar el árbol es:\n\n" ++ (viewSynTree st) );
                         mainLoop
                       }
                  else putStr "\nFin de la ejecución\n"
              }

-- FUNCIÓN PRINCIPAL --
main :: IO ()
main = do { hSetBuffering stdin LineBuffering; -- Permite que la entrada pueda ser editada
            mainLoop;                          -- Ejecutamos el "bucle" principal
          }

