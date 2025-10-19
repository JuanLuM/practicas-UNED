// Comentario de linea
{ comentario entre llaves }
{ Comentario con otro comentario {segundo comentario}}
{ comentario de varias lineas
	{linea 1}
	{linea 2}
	{...}
}

program uno;
const
        cierto = true;
        falso = false;
        uno = 1;
		dos = 2;
type
	unoAdiez = set of 1..10;
	tresAcinco = set of 3..5;
	unoAdos = set of uno..dos;
var a: integer;
	b,c: integer;
	esCierto,esFalso: boolean;
	conjunto : unoAdiez;
	
	// Prodecimiento vacio
	procedure vacio();
	begin
	end;
	// Prodecimiento sin parametros
	procedure escribir();
	begin
		write("Procedimiento 1");
	end;
	// Prodecimiento con 1 parametro
	procedure escribir2(z:integer);
	begin
		write("Procedimiento 2");
	end;
	// Prodecimiento con varios parametro
	procedure escribir3(x,y,z:integer);
	begin
		write("Procedimiento 2");
	end;
	// Prodecimiento con varios parametro
	procedure escribir3(x,y,z:integer;verdad:boolean);
	begin
		write("Procedimiento 2");
	end;
	// Funcion sin parametros con retorno integer
	function uno():integer;
	begin
		uno := 1;
	end;
	// Funcion sin parametros con retorno boolean
	function uno():boolean;
	begin
		verdad := true;
	end;
	// Funcion con varios parametros
	function escribir3(x,y,z:integer;verdad:boolean):boolean;
	begin
		verdad := true;
	end;
	// Subprogramas anidados
	procedure escribir4(x,y,z:integer;verdad:boolean);
		function esFalso():boolean;
			procedure escribir();
				procedure escribir();
				begin
					write("Procedimiento anidado 3");
				end;
			begin
				write("Procedimiento anidado 1");
			end;
		begin
			verdad := false;
		end;
	begin
		write("Procedimiento principal");
	end;
	
	// Subprograma con constante, variables y tipos
	function esFalso():boolean;
		const
				escierto = true;
				esfalso = false;
				tres = 3;
				cuatro = 4;
		type
			dosAseis = set of 2..6;
			tresAcuatro = set of tres..cuatro;
		var suma: integer;
			resta,producto: integer;
			conjunto : tresAcuatro;
		procedure escribir();
			procedure escribir();
				var resultado: integer;
			begin
				write("Procedimiento anidado 3");
			end;
		begin
			write("Procedimiento anidado 1");
		end;
	begin
		verdad := false;
	end;
begin
	a := 1;
	a := 1 + 1;
	a := 1 + 1 * (3);
	a := 1 + 1 * (3-2);
	a := b;
	a := true;
	a := 3<>4;
	esCierto := true;
	esFalso := false;
	esCierto := esFalso;
	
	// Asignacion conjuntos
	conjunto := [];
	conjunto := [1..2];
	
	// Llamada de procedimiento
	escribir();
	escribir(4);
	escribir(4,5);
	
	// Llamada a funcion
	a := escribir();
	a := escribir(1,2);
	a := escribir(1,funcion(c));
	
	// Sentencia if sin else
	if (a>b) then // Sin begin -end
		a := a + 1;
	
	if (a>b) then // Con begin -end
	begin
		a := a + 1;
	end;
		
	if (esCierto) then // Con variable boolean
	begin
		valor := true;
	end;
	
	if (esCierto Or (a>1)) then // Con condicion logica
	begin
		valor := true;
	end;
			
	if (uno()) then // Con llamada a funcion
		a := a + 1;
	
	if (a<>b) then // Con varias sentencias
	begin
		a := a + 1;
		b := b + 1;
	end;

	// If con else
	if (a>b) then // if y else sin begin - end
		a := a + 1;
	else
		a := a + 2;
		
	if (esCierto) then // Expresion boolean
		valor := true;
	else
		valor := false;
		
	if (a in conjunto) then // Expresion de conjuntos
		write("a en conjunto set");
	else
		write("a no pertenece a conjunto set");
		
	// If y else con begin - end y varias sentencias
	if (a>b) then
		begin
			a := a + 1;
			a := b + 2;
		end;
	else
		begin
			a := a + 1;
			a := b + 2;
		end;
	
	// If con Begin - end y else sin begin - end
	if (a<>b) then
	begin
		a := a + 1;
		b := b + 1;
	end;
	else
		a := a + 5;
		
	// If anidados
	if (a>b) then
	begin
		a := a + 1;
		if (a>b) then
		begin
			a := a + 1;
		end;
	end;
	
	// If con else ambiguo
	if (a>b) then
		a := a + 1;
		if (a>b) then
			a := a + 1;
	else
		a := a + 2;
				
	// Repeat
	repeat 
		a := a + 1;
	until (a>b);
	
	repeat 
		write("Ejemplo sentencia repeat 1");
	until (esCierto); // Con variable boolean
	

	repeat 
		write("Ejemplo sentencia repeat 1");
	until (uno()); // Con llamada a funcion

	repeat 
		a := a + 1;
		b := b + 1;
		c := 1 + 1 * (3-2);
		write("Ejemplo sentencia repeat 2");
	until (a>10);
	
	// Write
	write(1); 
	write(x);
	write(x-1);
	write(x);
	write("Hola mundo");
end.
