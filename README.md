# sentimentcategorization
Sentiment Categorization on a Creole Language with Lexicon-Based and Machine Learning Techniques

////////////////////////////////////////////////////////

TRABAJO FINAL DE GRADO
# Categorización de Sentimientos en Jopara: Técnicas Basadas en Léxico y en Aprendizaje de Máquina para una Mezcla de Lengua.
Autores: Adolfo Alfredo Ríos Núñez - Pedro José Amarilla Oviedo (adolforinu[at]]gmail.com, pj.amarilla[at]gmail.com)
Orientador: Dr. Ing. Gustavo Giménez Lugo (gustavo[at]dainf.ct.utfpr.edu.br)

///////////////////////////////////////////////////////

# ORGANIZACION DE ARCHIVOS:

1. Aprendizaje de Máquina - Carga Balanceada
	training_pos: Lista de tuits positivos (128 tuits)
	training_neg: Lista de tuits negativos (128 tuits)
	training_neu: Lista de tuits neutrales (128 tuits)
	entrada_weka: Datos de entrenamiento mapeados al formato de entrada requerido por Weka (arff)
	conjunto_evaluacion: Conjunto independiente de tuits de evaluación, mapeado en formato de Weka (arff)
	
2. Aprendizaje de Máquina - Carga Desbalanceada
	training_pos: Lista de tuits positivos (128 tuits)
	training_neg: Lista de tuits negativos (734 tuits)
	training_neu: Lista de tuits neutrales (640 tuits)
	entrada_weka: Datos de entrenamiento mapeados al formato de entrada requerido por Weka (arff)
	conjunto_evaluacion: Conjunto independiente de tuits de evaluación, mapeado en formato de Weka (arff)
	
3. Léxico
	positivas: Lista de palabras del léxico orientadas positivamente.
	negativas: Lista de palabras del léxico orientadas negativamente.
	
4. Ejecución en Java
	4.1 algorithms
		ArffGenerator: Generador de archivo de entrada de Weka a partir de lista de tuits.
		NaiveBayes: Implementación propia de Bayes Ingenuo con suavizado de Laplace.
		SimpleCount: Algoritmo de conteo simple (basado en léxico).
	4.2 sentimentanalysis
		CorpusReader: Lectura de un archivo de tuits seleccionados a partir del cual se generan los conjuntos de entrenamiento. Suma además la frecuencia de aparición de cada palabra.
		Preprocess: Aplicación de reglas de preprocesamiento establecidas en el modelado.
	4.3 wekaevaluation
		BayesClassifier: Ejecución automática de la implementación de Bayes Ingenuo de Weka con los archivos de entrada generados.
		MaxEntClassifier: Ejecución automática de las implementaciones de Entropía Máxima de Weka (Logistic y SimpleLog) con los archivos de entrada generados.
		SVMClassifier: Ejecución automática de la implementación de SVM de Weka (con sus distintas funciones de kernel) con los archivos de entrada generados.

