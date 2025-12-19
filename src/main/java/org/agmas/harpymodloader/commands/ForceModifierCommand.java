package org.agmas.harpymodloader.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.doctor4t.wathe.api.Role;
import dev.doctor4t.wathe.api.WatheRoles;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.agmas.harpymodloader.Harpymodloader;
import org.agmas.harpymodloader.commands.suggestions.ModifierSuggestionProvider;
import org.agmas.harpymodloader.commands.suggestions.RoleSuggestionProvider;
import org.agmas.harpymodloader.modifiers.HMLModifiers;
import org.agmas.harpymodloader.modifiers.Modifier;

public class ForceModifierCommand {
    public static final SimpleCommandExceptionType INVALID_ROLE_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.forcerole.invalid"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("forceModifier")
                .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .then(CommandManager.argument("player", EntityArgumentType.player())
                .then(CommandManager.argument("role", StringArgumentType.string())
                        .suggests(new ModifierSuggestionProvider())
                        .executes(context -> execute(context.getSource(), EntityArgumentType.getPlayer(context,"player"), StringArgumentType.getString(context,"role"))))));
    }

    private static int execute(ServerCommandSource source, ServerPlayerEntity targetPlayer, String roleName) throws CommandSyntaxException {
        for (Modifier modifier : HMLModifiers.MODIFIERS) {
            if (modifier.identifier().getPath().equals(roleName)) {
                Harpymodloader.addToForcedModifiers(modifier,targetPlayer);
                source.sendFeedback(() -> Text.translatable("commands.forcerole.success", Text.literal(roleName).withColor(modifier.color()), targetPlayer.getDisplayName()), true);
                return 1;
            }
        }

        throw INVALID_ROLE_EXCEPTION.create();
    }
}