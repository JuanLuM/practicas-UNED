module TestTLP2024Estudiantes where

import Data.Char
import System.IO
import StackMachine

type Test = (String,String,SynTree Integer,Integer)

tfile :: String
tfile = "TestTLP2024Estudiantes.txt"

--- CHECK A TEST ---
checkTest (n,e,t,r) = do { putStr ("Ejecutando el test: "++n++"...");
                           let et = createSynTree (words e) in
                             if (t /= et)
                             then putStr ("¡Error! El árbol generado no coincide con el esperado\n");
                             else if (r /= (evalSynTree et))
                                    then putStr ("¡Error! El resultado obtenido no coincide con el esperado\n");
                                    else putStr ("¡Test superado!\n");
                         }

--- MAIN LOOP ---
loop :: [String] -> IO ()
loop []     = putStr "End of test file\n"
loop (t:ts) = do { let test = (read t)::Test in do {
                     checkTest test;
                     loop ts;
                   }
                 }

--- FUNCTION MAIN ---
main :: IO ()
main = do { putStr ("Loading test file: " ++ tfile ++ "\n");  -- INDICAMOS QUE VAMOS A LEER EL FICHERO CON LOS TESTS
            hFlush stdout;                                    -- PROVOCA QUE LA SALIDA SE VUELQUE EN PANTALLA
            fcontent <- (readFile tfile);                     -- LEEMOS EL CONTENIDO DEL FICHERO
            let tests = (lines fcontent) in do {              -- CADA LÍNEA ES UN TEST
              loop tests;                                     -- EJECUTA EL BUCLE PRINCIPAL SOBRE LOS TESTS
            }
          }

