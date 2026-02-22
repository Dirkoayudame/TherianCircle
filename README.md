¡• Therian Circle es una app Android (Java + XML) tipo red social para comunidad therian.

  Qué hace hoy:

  - Registro e inicio de sesión con Firebase Auth.
  - Navegación por 5 secciones: Feed, Grupos, Eventos, Chat y Perfil.
  - Feed social:
      - crear publicaciones,
      - dar/quitar like,
      - comentar.
  - Grupos:
      - unirte y salir.
  - Eventos:
      - confirmar asistencia (RSVP) y cancelar.
  - Chat:
      - envío de mensajes,
      - actualización periódica tipo “casi tiempo real”.
  - Notificaciones locales:
      - avisan mensajes nuevos de otros usuarios.
  - Perfil:
      - muestra identidad básica y permite cerrar sesión.

  Base técnica:

  - Arquitectura por capas con repositorio.
  - Persistencia offline con Room (cache local).
  - Cache en memoria + local + remoto mock para datos sociales.
  - UI con RecyclerView y estados de carga/vacío/error.

  En resumen: ya es un MVP funcional social/chat con sesión, interacciones y persistencia local.

