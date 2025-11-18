# 🍹 DrinkFinder - Buscador de Bebidas

DrinkFinder es una aplicación móvil desarrollada en **Android Studio** que facilita la búsqueda de bebidas, mostrando ingredientes, recetas relacionadas y opciones de disponibilidad por región. Su objetivo es ofrecer una experiencia práctica, escalable y con impacto real, integrando todos los conocimientos de desarrollo Android desde el tema 4 al tema 15.

---

## 🎯 Objetivo del Proyecto

Desarrollar una aplicación Android que integre todos los conocimientos aprendidos en el primer y segundo parcial, aplicando los temas 4 al 10 como mínimo y extendiendo hasta el tema 15. La aplicación permitirá:

-  🔍 Buscar bebidas por nombre o por ingrediente usando fragmentos maestro-detalle
-  📋 Mostrar ingredientes de cada receta con interfaz gráfica avanzada
-  🛎️ Notificar al usuario mediante servicios cuando un ingrediente esté en descuento
-  🍸 Clasificar bebidas en dos categorías: con alcohol / sin alcohol
-  🌎 Disponibilidad multilingüe y multinacional
-  🔄 Cambio de país automático cuando una bebida no esté disponible en la región
-  📱 Integración con aplicaciones externas y servicios del sistema
-  🗄️ Persistencia de datos usando bases de datos locales
-  🎵 Reproducción de sonidos y multimedia relacionados con las bebidas
-  📍 Mapeo para mostrar bares y tiendas cercanas
-  📊 Uso de sensores para interacciones avanzadas

---

## 📚 Requerimientos Académicos

### Temas Implementados (Obligatorios):

#### **Tema 4. Fragmentos, flujo maestro-detalle y menú**

-  ✅ Implementación de fragmentos para navegación
-  ✅ Patrón maestro-detalle para lista de bebidas y detalles
-  ✅ Menú de navegación con drawer layout
-  ✅ ActionBar y Toolbar personalizados

#### **Tema 5. Elementos de interfaz gráfica**

-  ✅ RecyclerView para listas de bebidas
-  ✅ CardView para presentación de información
-  ✅ ImageView con carga de imágenes desde API
-  ✅ EditText con validaciones personalizadas
-  ✅ Button, CheckBox, RadioButton para filtros

#### **Tema 6. Más sobre interfaz gráfica**

-  ✅ Layouts complejos (ConstraintLayout, CoordinatorLayout)
-  ✅ ViewPager2 con TabLayout para categorías
-  ✅ FloatingActionButton para acciones rápidas
-  ✅ BottomSheet para opciones adicionales
-  ✅ Snackbar y Toast para feedback al usuario

#### **Tema 7. Transiciones**

-  ✅ Animaciones entre actividades y fragmentos
-  ✅ Shared Element Transitions para imágenes
-  ✅ Animaciones de entrada y salida personalizadas
-  ✅ Motion Layout para animaciones complejas

#### **Tema 8. Uso de aplicaciones externas**

-  ✅ Intent implícito para compartir recetas
-  ✅ Integración con aplicaciones de mapas
-  ✅ Apertura de enlaces web en navegador
-  ✅ Intent para llamadas telefónicas a establecimientos

#### **Tema 9. Emisiones, hilos y servicios**

-  ✅ AsyncTask/ExecutorService para operaciones en background
-  ✅ Handler y Looper para comunicación entre hilos
-  ✅ BroadcastReceiver para notificaciones del sistema
-  ✅ WorkManager para tareas programadas

#### **Tema 10. Servicios**

-  ✅ Foreground Service para sincronización de datos
-  ✅ IntentService para descargas de imágenes
-  ✅ Bound Service para reproducción de audio
-  ✅ JobScheduler para tareas periódicas

### Temas Adicionales (Opcionales):

#### **Tema 11. Bases de datos**

-  ✅ Room Database para persistencia local
-  ✅ SQLite para almacenamiento de favoritos
-  ✅ Content Provider para compartir datos
-  ✅ Migrations para actualizaciones de esquema

#### **Tema 12. Multimedia**

-  ✅ MediaPlayer para sonidos de notificación
-  ✅ Reproducción de videos de preparación
-  ✅ Captura de fotos personalizadas
-  ✅ Galería de imágenes de cócteles

#### **Tema 13. Mapeo**

-  ✅ Google Maps integration
-  ✅ Geolocalización de bares cercanos
-  ✅ Marcadores personalizados
-  ✅ Rutas hacia establecimientos

#### **Tema 14. Sensores**

-  ✅ Acelerómetro para agitar y mezclar
-  ✅ Sensor de proximidad para interacciones
-  ✅ Giroscopio para rotación de vista 3D
-  ✅ Sensor de luz ambiental para tema automático

#### **Tema 15. Publicación en Google Play**

-  ✅ Preparación del APK/AAB
-  ✅ Firma digital de la aplicación
-  ✅ Metadata y assets para la tienda
-  ✅ Configuración de versiones y actualizaciones

---

## 🛠️ Especificaciones Técnicas

### **Plataforma y Versiones**

-  **Sistema Operativo**: Android 9.0 (API level 28) mínimo
-  **Arquitectura**: MVVM (Model-View-ViewModel) con Clean Architecture
-  **Lenguaje**: Java 8+ con compatibilidad para Android
-  **IDE**: Android Studio Electric Eel o superior
-  **Gradle**: 7.4.0 o superior
-  **Target SDK**: API 34 (Android 14)
-  **Compile SDK**: API 34

### **Dependencias Principales**

```gradle
// UI Components
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'androidx.fragment:fragment:1.6.2'
implementation 'androidx.recyclerview:recyclerview:1.3.2'
implementation 'androidx.cardview:cardview:1.0.0'
implementation 'androidx.viewpager2:viewpager2:1.0.0'
implementation 'com.google.android.material:material:1.10.0'

// Architecture Components
implementation 'androidx.lifecycle:lifecycle-viewmodel:2.7.0'
implementation 'androidx.lifecycle:lifecycle-livedata:2.7.0'
implementation 'androidx.room:room-runtime:2.6.0'
implementation 'androidx.work:work-runtime:2.8.1'

// Networking
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation 'com.squareup.okhttp3:logging-interceptor:4.11.0'

// Image Loading
implementation 'com.github.bumptech.glide:glide:4.15.1'

// Maps and Location
implementation 'com.google.android.gms:play-services-maps:18.2.0'
implementation 'com.google.android.gms:play-services-location:21.0.1'

// Multimedia
implementation 'androidx.media:media:1.7.0'
implementation 'com.google.android.exoplayer:exoplayer:2.19.1'
```

### **Permisos Requeridos**



---

## 🧾 Justificación

El desarrollo de DrinkFinder permite:

1. **Aplicación práctica de conocimientos**: Implementar todos los temas del curso (4-15) en un proyecto real
2. **Comprensión del ciclo de vida completo**: Desde el diseño hasta la publicación en Google Play
3. **Dominio de herramientas de la industria**: Android Studio, Java, Room Database, Retrofit, Google Maps
4. **Experiencia con arquitecturas profesionales**: MVVM, Clean Architecture, patrones de diseño
5. **Integración de servicios modernos**: APIs REST, servicios en background, notificaciones push
6. **Desarrollo de habilidades avanzadas**: Manejo de sensores, multimedia, mapas y bases de datos
7. **Preparación para el mercado laboral**: Código siguiendo estándares de la industria

---

## 🛠️ Propuesta de Solución

La solución planteada consiste en una app móvil robusta con:

### **Arquitectura y Diseño**

-  🏗️ **Arquitectura MVVM** con Clean Architecture para separación de responsabilidades
-  🎨 **Material Design 3** para una interfaz moderna y consistente
-  📱 **Responsive Design** adaptable a tablets y diferentes densidades de pantalla
-  🔄 **Offline-first approach** con sincronización automática

### **Funcionalidades Core**

-  🔍 **Búsqueda inteligente** con filtros avanzados y sugerencias
-  📋 **Gestión de favoritos** con persistencia local
-  🛎️ **Sistema de notificaciones** con WorkManager para ofertas y recordatorios
-  🍸 **Categorización automática** de bebidas alcohólicas y no alcohólicas
-  🌐 **Soporte multiidioma** con recursos localizados

### **Integración con Servicios**

-  🌐 **API TheCocktailDB** para datos de bebidas actualizados
-  📍 **Google Maps** para localización de establecimientos
-  📱 **Intents implícitos** para compartir recetas y llamar establecimientos
-  🎵 **MediaPlayer** para sonidos ambiente y efectos

### **Calidad y Testing**

-  ✅ **Unit Tests** con JUnit y Mockito
-  ✅ **UI Tests** con Espresso
-  ✅ **Integration Tests** para APIs y base de datos
-  📊 **Code Coverage** mínimo del 80%

## se puede ocupar https://www.thecocktaildb.com/api.php como fuente de información para las bebidas

---

## 📦 Alcance del Proyecto

### **Funcionalidades Principales**

#### **Módulo de Búsqueda y Navegación** (Temas 4-5)

-  🏗️ Fragment-based navigation con Navigation Component
-  📱 Master-detail flow para lista de bebidas y detalles
-  🎯 Drawer navigation con menú categorizado
-  🔍 SearchView con filtros avanzados (por ingrediente, tipo, región)
-  📊 RecyclerView con diferentes tipos de vista (grid/list)

#### **Interfaz Avanzada** (Temas 5-6)

-  💾 ViewPager2 con TabLayout para categorías (Con alcohol/Sin alcohol)
-  🎨 CardView con elevation y ripple effects
-  ⚡ FloatingActionButton para acciones rápidas (favoritos, compartir)
-  📱 BottomSheet para opciones de filtrado
-  🎯 CoordinatorLayout con scrolling behaviors
-  📐 ConstraintLayout para layouts complejos y responsive

#### **Animaciones y Transiciones** (Tema 7)

-  ✨ Shared Element Transitions para imágenes de bebidas
-  🎭 Fragment transitions personalizadas
-  🔄 MotionLayout para animaciones complejas de preparación
-  📱 Activity transitions con slide/fade effects
-  🎨 Property animations para feedback visual

#### **Integración Externa** (Tema 8)

-  � Intent implícito para compartir recetas en redes sociales
-  📞 Intent para realizar llamadas a establecimientos
-  🌐 Apertura de sitios web de bares y tiendas
-  📧 Email para enviar listas de compras de ingredientes
-  📍 Integración con aplicaciones de mapas para navegación

#### **Servicios y Background Tasks** (Temas 9-10)

-  ⚙️ Foreground Service para sincronización de datos de API
-  🔄 WorkManager para actualizaciones periódicas de ofertas
-  📨 FCM para notificaciones push de nuevas bebidas
-  � Bound Service para reproducción de música ambiente
-  📡 BroadcastReceiver para detectar cambios de conectividad
-  ⏰ JobScheduler para limpieza de caché automática

#### **Almacenamiento de Datos** (Tema 11)

-  🗄️ Room Database para persistencia local completa
-  💾 SQLite para almacenamiento de favoritos y historial
-  🔄 Repository pattern para abstracción de datos
-  📊 Content Provider para compartir datos con otras apps
-  🔧 Database migrations para actualizaciones de esquema

#### **Multimedia** (Tema 12)

-  🎵 MediaPlayer para sonidos de notificación personalizados
-  🎬 ExoPlayer para videos de preparación de cócteles
-  📷 Camera API para capturar fotos de creaciones personales
-  🖼️ Galería integrada para navegación de imágenes
-  🎤 Grabación de notas de voz para recetas personales

#### **Mapas y Ubicación** (Tema 13)

-  �️ Google Maps SDK integration
-  📍 Geolocalización para encontrar bares cercanos
-  📌 Marcadores personalizados para diferentes tipos de establecimientos
-  🧭 Navegación GPS hacia ubicaciones seleccionadas
-  📐 Cálculo de distancias y rutas optimizadas

#### **Sensores** (Tema 14)

-  📱 Acelerómetro para detectar "agitado" de cóctel
-  �️ Sensor de proximidad para pausar videos automáticamente
-  🌀 Giroscopio para vista 360° de presentación de bebidas
-  💡 Sensor de luz ambiental para cambio automático de tema
-  🌡️ Sensor de temperatura para sugerencias estacionales

#### **Distribución** (Tema 15)

-  📦 Configuración para Android App Bundle (AAB)
-  🔐 Firma digital con keystore de producción
-  📋 Metadata completa para Google Play Console
-  🎯 Configuración de release/debug builds
-  📈 Preparación para analytics y crash reporting

### **Arquitectura del Sistema**

```
┌─────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                   │
├─────────────────────────────────────────────────────────┤
│ Activities │ Fragments │ ViewModels │ Adapters │ Views  │
├─────────────────────────────────────────────────────────┤
│                     DOMAIN LAYER                        │
├─────────────────────────────────────────────────────────┤
│ Use Cases  │ Entities  │ Repository Interfaces          │
├─────────────────────────────────────────────────────────┤
│                      DATA LAYER                         │
├─────────────────────────────────────────────────────────┤
│ API Service│Room DB    │ Shared Prefs │ Repository Impl │
└─────────────────────────────────────────────────────────┘
```

### **No Incluye**

-  � Backend personalizado (se usa API pública)
-  🚫 Autenticación con OAuth (solo local)
-  🚫 Pagos in-app o compras
-  🚫 Chat o mensajería en tiempo real
-  🚫 Integración con redes sociales avanzada

---

## 👥 Beneficiarios

-  👩‍💻 **Estudiantes de desarrollo Android** → Proyecto integral que cubre todos los temas del curso con implementación práctica
-  🧑‍🎓 **Profesores y evaluadores** → Evidencia clara de competencias adquiridas en cada tema específico
-  🧑‍🤝‍🧑 **Usuarios finales** → Aplicación funcional, intuitiva y práctica para descubrir y preparar bebidas
-  🏫 **Institución universitaria** → Proyecto de alta calidad como referencia para futuras generaciones
-  💼 **Empleadores potenciales** → Demostración de habilidades técnicas y capacidad de desarrollo completo

---

## 📱 Stack Tecnológico

### **Desarrollo**

-  **Lenguaje**: Java 8+ con Android SDK
-  **IDE**: Android Studio Electric Eel (2022.1.1) o superior
-  **Build System**: Gradle 7.4+ con Android Gradle Plugin 7.4+
-  **Arquitectura**: MVVM + Clean Architecture + Repository Pattern

### **UI/UX**

-  **Design System**: Material Design 3 (Material You)
-  **Layouts**: XML con ConstraintLayout, CoordinatorLayout, MotionLayout
-  **Navigation**: Navigation Component con Safe Args
-  **Theming**: Dynamic Colors con soporte para modo oscuro

### **Networking & APIs**

-  **HTTP Client**: Retrofit 2.9+ con OkHttp3
-  **JSON Parsing**: Gson converter
-  **Image Loading**: Glide 4.15+ con transformaciones y caché
-  **API**: [TheCocktailDB](https://www.thecocktaildb.com/api.php)

### **Almacenamiento**

-  **Base de Datos**: Room 2.6+ (SQLite wrapper)
-  **Preferencias**: SharedPreferences con Preference DataStore
-  **Caché**: Glide disk cache + custom HTTP cache

### **Servicios y Background**

-  **Background Tasks**: WorkManager 2.8+
-  **Servicios**: Foreground Services con NotificationCompat
-  **Notificaciones**: Firebase Cloud Messaging (FCM)

### **Multimedia y Sensores**

-  **Audio**: MediaPlayer + ExoPlayer 2.19+
-  **Cámara**: Camera2 API con ImageCapture
-  **Mapas**: Google Maps SDK 18.2+
-  **Ubicación**: Google Location Services 21.0+
-  **Sensores**: Android Sensor Framework

### **Testing y Calidad**

-  **Unit Testing**: JUnit 4/5 + Mockito + Truth
-  **UI Testing**: Espresso + UI Automator
-  **Architecture Testing**: Room testing + LiveData testing
-  **Code Quality**: SonarQube + Android Lint

### **Deployment**

-  **Signing**: Android keystore con firma de release
-  **Distribution**: Google Play Console
-  **Format**: Android App Bundle (AAB)
-  **Versioning**: Semantic versioning (versionCode + versionName)

---

## 🚀 Roadmap de Desarrollo

### **Fase 1: Configuración y Arquitectura** (Evidencia 2)

-  ✅ Setup del proyecto con estructura modular
-  ✅ Configuración de dependencias y Gradle
-  ✅ Implementación de arquitectura base (MVVM)
-  ✅ Setup de Room Database y Repository
-  ✅ Configuración de Retrofit para API calls
-  ✅ Implementación de fragmentos principales
-  ✅ Navigation Component setup
-  ✅ Master-detail flow para bebidas
-  ✅ Material Design theme y componentes base
-  ✅ RecyclerView con ViewHolder pattern

### **Fase 3: Funcionalidades Core** (Evidencia 2)

-  ✅ Integración con TheCocktailDB API
-  ✅ Sistema de búsqueda y filtros
-  ✅ Gestión de favoritos con Room
-  ✅ Carga de imágenes con Glide
-  ✅ Implementación de ViewPager2 con tabs
-  ✅ WorkManager para sincronización
-  ✅ Foreground services para actualizaciones
-  ✅ Sistema de notificaciones
-  ✅ BroadcastReceiver para conectividad
-  ✅ Intent handling para compartir

### **Fase 5: Multimedia y Sensores** (Evidencia 3)

-  ✅ Integración de MediaPlayer
-  ✅ Camera API para fotos personales
-  ✅ Sensor de acelerómetro para interacciones
-  ✅ Google Maps para ubicaciones
-  ✅ Animaciones y transiciones avanzadas

### **Fase 6: Testing y Refinamiento** (Pendiente)

-  ✅ Unit tests para ViewModels y Repository
-  ✅ UI tests con Espresso
-  ✅ Performance optimization
-  ✅ Accessibility improvements
-  ✅ Code review y refactoring

### **Fase 7: Deployment** (Evidencia 3)

-  ✅ Preparación para release build
-  ✅ Firma digital y keystore setup
-  ✅ Play Console metadata y assets
-  ✅ Beta testing con Play Console
-  ✅ Documentación final y entrega

---

## 📚 Criterios de Evaluación

### **Implementación por Tema (70%)**

-  **Tema 4 (10%)**: Fragmentos, master-detail, menú navigation
-  **Tema 5 (10%)**: RecyclerView, CardView, Material components
-  **Tema 6 (10%)**: Layouts avanzados, ViewPager, animations
-  **Tema 7 (5%)**: Transiciones entre pantallas y elementos
-  **Tema 8 (5%)**: Intents implícitos y comunicación con apps externas
-  **Tema 9 (10%)**: Threading, AsyncTask/ExecutorService, handlers
-  **Tema 10 (10%)**: Servicios foreground/background, WorkManager
-  **Tema 11 (5%)**: Room Database, Content Providers
-  **Tema 12 (2.5%)**: MediaPlayer, manejo de multimedia
-  **Tema 13 (2.5%)**: Google Maps, geolocalización

### **Calidad de Código (20%)**

-  Arquitectura limpia y mantenible
-  Principios SOLID aplicados
-  Manejo adecuado de errores
-  Code style y documentación
-  Performance y memory management

### **Funcionalidad y UX (10%)**

-  App completamente funcional
-  Interfaz intuitiva y responsive
-  Flujo de usuario coherente
-  Manejo de estados y loading
-  Offline capabilities

---

## 📖 Documentación Adicional

-  📋 **Manual de Usuario**: Guía completa de uso de la aplicación
-  🛠️ **Documentación Técnica**: Arquitectura, APIs y componentes
-  🧪 **Plan de Pruebas**: Casos de prueba y resultados
-  📱 **Guía de Instalación**: Setup del entorno de desarrollo
-  🚀 **Manual de Deployment**: Proceso de publicación en Play Store

---
