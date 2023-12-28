SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+01:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;


CREATE TABLE `joueur` (
  `id` int(10) UNSIGNED NOT NULL,
  `pseudo` varchar(50) DEFAULT NULL,
  `photo_profil` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `partie` (
  `id` int(10) UNSIGNED NOT NULL,
  `duree_secondes` int(10) UNSIGNED DEFAULT NULL,
  `taille_grille` int(10) UNSIGNED DEFAULT NULL,
  `nb_coups` int(11) NOT NULL,
  `timestamp` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `partie_competitive` (
  `id_joueur` int(10) UNSIGNED NOT NULL,
  `id_partie` int(10) UNSIGNED NOT NULL,
  `id_vainqueur` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `partie_cooperative` (
  `id_partie` int(10) UNSIGNED NOT NULL,
  `id_joueur` int(10) UNSIGNED NOT NULL,
  `nb_coups` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


ALTER TABLE `joueur`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id_UNIQUE` (`id`);

ALTER TABLE `partie`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id_UNIQUE` (`id`);

ALTER TABLE `partie_competitive`
  ADD PRIMARY KEY (`id_joueur`,`id_partie`),
  ADD KEY `id_partie2` (`id_partie`),
  ADD KEY `id_joueur2` (`id_joueur`),
  ADD KEY `id_vainqueur` (`id_vainqueur`);

ALTER TABLE `partie_cooperative`
  ADD PRIMARY KEY (`id_partie`,`id_joueur`),
  ADD KEY `fk_partie_has_joueur_joueur1_idx` (`id_joueur`),
  ADD KEY `fk_partie_has_joueur_partie_idx` (`id_partie`);


ALTER TABLE `joueur`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

ALTER TABLE `partie`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;


ALTER TABLE `partie_competitive`
  ADD CONSTRAINT `id_joueur_2` FOREIGN KEY (`id_joueur`) REFERENCES `joueur` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `id_partie_2` FOREIGN KEY (`id_partie`) REFERENCES `partie` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `id_vainqueur` FOREIGN KEY (`id_vainqueur`) REFERENCES `joueur` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `partie_cooperative`
  ADD CONSTRAINT `id_joueur` FOREIGN KEY (`id_joueur`) REFERENCES `joueur` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `id_partie` FOREIGN KEY (`id_partie`) REFERENCES `partie` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
